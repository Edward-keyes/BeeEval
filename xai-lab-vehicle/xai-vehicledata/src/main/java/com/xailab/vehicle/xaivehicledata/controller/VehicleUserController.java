package com.xailab.vehicle.xaivehicledata.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.request.AccountRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.LoginResponse;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserService;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicleuser")
public class VehicleUserController {
    @Resource
    VehicleUserService vehicleUserService;

    /**
     * 创建普通账号
     */
    @PostMapping("/signUpAccount")
    public Result signUpAccount(@RequestBody AccountRequest adminAccount){

        return vehicleUserService.signUpAccount(adminAccount);

    }

    /**
     * 登录账号
     */
    @PostMapping("/doLogin")
    public Result<LoginResponse> doLogin(@RequestBody AccountRequest adminAccount){

        return vehicleUserService.login(adminAccount);

    }

    /**
     * 获取账号登录状态
     */
    @PostMapping("/isLogin")
    public Result<Boolean> isLogin(){

        return Result.ok(StpUtil.isLogin());

    }

    /**
     * 退出登录
     */
    @SaCheckLogin
    @GetMapping("/logout")
    public Result logout(){

        // 当前会话注销登录
        StpUtil.logout();

        return Result.ok();

    }

}
