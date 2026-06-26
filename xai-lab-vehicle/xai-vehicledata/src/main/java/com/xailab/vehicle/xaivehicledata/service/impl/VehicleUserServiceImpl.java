package com.xailab.vehicle.xaivehicledata.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.PasswordUtil;
import com.xailab.vehicle.xaicommon.utils.RandomUtil;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.dao.VehicleUserDao;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserEntity;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserLogEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.AccountRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.LoginResponse;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserLogService;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service("vehicleUserService")
public class VehicleUserServiceImpl extends ServiceImpl<VehicleUserDao, VehicleUserEntity> implements VehicleUserService {

    @Autowired
    private VehicleUserDao vehicleUserDao;

    SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(0, 0);

    @Autowired
    private VehicleUserLogService vehicleUserLogService;

    @Override
    public Result signUpAccount(AccountRequest adminAccount) {

        String email = adminAccount.getEmail();

        VehicleUserEntity vehicleUser = vehicleUserDao.selectOne(new QueryWrapper<VehicleUserEntity>().eq("email", email));

        if (Objects.nonNull(vehicleUser)) {
            return Result.error(4010,"Email already exists");
        }

        //TODO: 判断验证码是否正确 接入Redis

        //生成盐值
        String salt = RandomUtil.generateRandomString(5);
        String newPassword =null;
        String password = null;
        if (Objects.isNull(adminAccount.getPassword())){
            //随机生成5位数密码
            newPassword = RandomUtil.generateRandomString(5);
            password=PasswordUtil.encryptPassword(newPassword, salt);
        }else {
            //加密密码
            password=PasswordUtil.encryptPassword(adminAccount.getPassword(), salt);

        }
        VehicleUserEntity newVehicleUser = new VehicleUserEntity();
        newVehicleUser.setEmail(email);
        newVehicleUser.setPassword(password);
        newVehicleUser.setSalt(salt);
        newVehicleUser.setTeam("admin");
        newVehicleUser.setAccountType(0);
        newVehicleUser.setStatus(1);
        newVehicleUser.setCreateDate(new Date());
        newVehicleUser.setId(idWorker.nextId());
        int insert = vehicleUserDao.insert(newVehicleUser);

        if (insert == 1) {
            return Result.ok("Sign up success!"+"new password:"+newPassword);
        }else{
            return Result.error(4011,"Sign up failed");
        }
    }

    @Override
    public Result<LoginResponse> login(AccountRequest adminAccount) {

        VehicleUserEntity userEntity = vehicleUserDao.selectOne(new QueryWrapper<VehicleUserEntity>().eq("email",adminAccount.getEmail()));

        if (Objects.isNull(userEntity)){
            return Result.error(4062,"邮箱未注册");
        }

        if (PasswordUtil.matchingPassword(adminAccount.getPassword(),userEntity.getPassword(),userEntity.getSalt())){
            StpUtil.login(userEntity.getId());
            VehicleUserLogEntity vehicleUserLogEntity = new VehicleUserLogEntity();
            vehicleUserLogEntity.setCreateDate(new Date());
            vehicleUserLogEntity.setUserId(userEntity.getId());
            vehicleUserLogEntity.setOperation("登录");
            vehicleUserLogEntity.setId(idWorker.nextId());
            vehicleUserLogService.saveLog(vehicleUserLogEntity);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(StpUtil.getTokenValue());
            loginResponse.setStatus(userEntity.getStatus());
            loginResponse.setTeam(userEntity.getTeam());
            return Result.ok(loginResponse);
        }else{
            return Result.error(4061,"登录失败，密码错误");
        }

    }
}
