package com.xailab.vehicle.xaimessage.controller;

import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaimessage.service.SendMailService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    SendMailService sendMailService;

    @RequestMapping("/hello")
    public R hello() {

        return R.ok("Hello World!").put("data", "This is a test message.");

    }

    @RequestMapping("/send")
    public Result send(@RequestParam("email") String email, @RequestParam("password") String password) {

        return Result.ok(sendMailService.newSendCode(email,password));

    }

    @RequestMapping("/sendEn")
    public Result sendEn(@RequestParam("email") String email, @RequestParam("password") String password) {

        return Result.ok(sendMailService.newSendEnCode(email,password));

    }

    @RequestMapping("/sendJp")
    public Result sendJp(@RequestParam("email") String email, @RequestParam("password") String password) {

        return Result.ok(sendMailService.newSendJpCode(email,password));

    }

    @RequestMapping("/sendZhengShi")
    public Result sendZhengShi(@RequestParam("email") String email, @RequestParam("password") String password) {

        return Result.ok(sendMailService.sendZhengShi(email,password));

    }

    @RequestMapping("/sendZhengShiTest")
    public Result sendZhengShiTest(@RequestParam("email") String email, @RequestParam("password") String password) {

        return Result.ok(sendMailService.sendZhengShiTest(email,password));

    }

}
