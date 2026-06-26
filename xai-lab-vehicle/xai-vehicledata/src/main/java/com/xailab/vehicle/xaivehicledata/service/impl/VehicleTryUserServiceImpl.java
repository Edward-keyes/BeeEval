package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.dao.VehicleTryUserDao;
import com.xailab.vehicle.xaivehicledata.entity.VehicleTryUserEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleTryUserService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("vehicleTryUserService")
public class VehicleTryUserServiceImpl extends ServiceImpl<VehicleTryUserDao, VehicleTryUserEntity>
        implements VehicleTryUserService {

    private static final SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(0, 0);

    @Override
    public Result saveTryUser(VehicleTryUserEntity tryUser) {
        try {
            tryUser.setId(snowflakeIdGenerator.nextId());
            tryUser.setCreateData(new Date());
            save(tryUser);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("保存试用记录失败：" + e.getMessage());
        }
    }

    @Override
    public Result getTryUserList() {
        try {
            QueryWrapper<VehicleTryUserEntity> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("create_data");
            List<VehicleTryUserEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取试用记录列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<VehicleTryUserEntity> getTryUserListByUserId(Long userId) {
            QueryWrapper<VehicleTryUserEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                    .orderByDesc("create_data");
            List<VehicleTryUserEntity> list = list(wrapper);
            return list;
    }

    @Override
    public Result getTryUserListByVehicleId(Long vehicleId) {
        try {
            QueryWrapper<VehicleTryUserEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("vehicle_id", vehicleId)
                    .orderByDesc("create_data");
            List<VehicleTryUserEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取车辆试用记录列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result getTryUserByUserIdAndVehicleId(Long userId, Long vehicleId) {
        try {
            QueryWrapper<VehicleTryUserEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                    .eq("vehicle_id", vehicleId);
            VehicleTryUserEntity tryUser = getOne(wrapper);
            return Result.ok(tryUser);
        } catch (Exception e) {
            return Result.error("获取试用记录失败：" + e.getMessage());
        }
    }
}