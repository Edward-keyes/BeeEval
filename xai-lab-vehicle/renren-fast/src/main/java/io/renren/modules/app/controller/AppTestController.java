/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.app.controller;


import com.google.protobuf.Api;
import io.renren.common.utils.R;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.annotation.LoginUser;
import io.renren.modules.app.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP测试接口
 *
 * 
 */
@RestController
@RequestMapping("/app")
//@Api("APP测试接口") // 这个可以保持不变
public class AppTestController {

    @Login
    @GetMapping("userInfo")
    @Operation(summary = "获取用户信息")  // 使用 @Operation 替代 @ApiOperation
    public R userInfo(@LoginUser UserEntity user){
        return R.ok().put("user", user);
    }

    @Login
    @GetMapping("userId")
    @Operation(summary = "获取用户ID")  // 使用 @Operation 替代 @ApiOperation
    public R userInfo(@RequestAttribute("userId") Integer userId){
        return R.ok().put("userId", userId);
    }

    @GetMapping("notToken")
    @Operation(summary = "忽略Token验证测试")  // 使用 @Operation 替代 @ApiOperation
    public R notToken(){
        return R.ok().put("msg", "无需token也能访问。。。");
    }

}
