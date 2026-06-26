package com.xailab.vehicle.xaivehicledata.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.xaicommon.utils.*;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.OssUploadRequest;
import com.xailab.vehicle.xaivehicledata.entity.vo.UserPVo;
import com.xailab.vehicle.xaivehicledata.feign.XaiMessageFeignService;
import com.xailab.vehicle.xaivehicledata.service.FunctionOneTagService;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("imputTableData")
@RefreshScope
@Slf4j
public class TestController {

    @Autowired
    XaiMessageFeignService xaiMessageFeignService;

    @Resource
    private FunctionOneTagService functionOneTagService;

    @Resource
    private VehicleUserService vehicleUserService;

    @Value("${vehicledata.user.id}")
    private String id;

    @Value("${vehicledata.user.name}")
    private String name;

    @Value("${vehicledata.user.age}")
    private String age;

    @RequestMapping("/hello")
    public R hello() {

        R hello = xaiMessageFeignService.hello();

        return R.ok().put("message", "Hello, XAI Vehicle Data!").put("msg",hello.get("data"))
                .put("info", "id:" + id + ",name:" + name + ",age:" + age);
    }

    @RequestMapping("/send")
    public Result send(@RequestParam("email") String email, @RequestParam("password") String password) {
        return xaiMessageFeignService.send(email,password);
    }

    @RequestMapping("/sendEn")
    public Result sendEn(@RequestParam("email") String email, @RequestParam("password") String password) {
        return xaiMessageFeignService.sendEn(email,password);
    }

    @RequestMapping("/sendJp")
    public Result sendJp(@RequestParam("email") String email, @RequestParam("password") String password) {
        return xaiMessageFeignService.sendJp(email,password);
    }

    @RequestMapping("/sendZh")
    public Result sendZh(@RequestParam("email") String email, @RequestParam("password") String password) {
        return xaiMessageFeignService.sendZhengShi(email,password);
    }

    @RequestMapping("/sendZhTest")
    public Result sendZhTest(@RequestParam("email") String email, @RequestParam("password") String password) {
        return xaiMessageFeignService.sendZhengShiTest(email,password);
    }

    /**
     * 导入功能树标签
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/functionTreeTag",consumes = "multipart/form-data")
    public R input(@RequestPart("file") MultipartFile file) throws Exception {

//        Map<String, JSONArray> map = ExcelUtils.readFileManySheet(file);
//        map.forEach((key, value) -> {
//            System.out.println("Sheet名称：" + key);
//            System.out.println("Sheet数据：" + value);
//            System.out.println("----------------------");
//        });

        functionOneTagService.inputFunctionTree(file);

        return R.ok().put("message", "File uploaded successfully!");

    }

    /**
     * 导入车辆功能树数据
     * @param path
     * @return
     */
    @RequestMapping("/functionTreeData")
    public R functionTreeData(@RequestPart("path") String path) {

        System.out.println(path);

        functionOneTagService.functionTreeData(path);

        return R.ok().put("message", "File uploaded successfully!");
    }

    /**
     * 导入功能域数据
     */
    @RequestMapping("/functionDomainData")
    public R functionDomainData(@RequestPart("path") String path) {

        System.out.println(path);

        functionOneTagService.functionDomainData(path);

        return R.ok().put("message", "File uploaded successfully!");
    }

    /**
     * 导入功能域新数据
     */
    @RequestMapping("/functionDomainNewData")
    public R functionDomainNewData(@RequestPart("path") String path) {
        System.out.println(path);

        functionOneTagService.functionDomainNewData(path);

        return R.ok().put("message", "File uploaded successfully!");
    }

    /**
     * 闻王昌龄左迁龙标有此寄
     * 唐·李白
     * 杨花落尽子规啼
     * 闻道龙标过五溪
     * 我寄愁心与明月
     * 随风直到夜郎西
     */

    /**
     * 一次性开放所有账号的正式账号权限7天
     */
    @GetMapping("/openAllAccount")
    public R openAllAccount() {

        List<VehicleUserEntity> users = vehicleUserService
                .list(new QueryWrapper<VehicleUserEntity>()
                        .eq("status", 1));

        /**
         * 发送邮件list
         */
        List<UserPVo> userPVoList = new ArrayList<>();

        /**
         * 修改数据list
         */
        for (VehicleUserEntity user : users) {

            //生成盐值
            String salt = RandomUtil.generateRandomString(5);
            String newPassword;
            String password;
            //随机生成5位数密码
            newPassword = RandomUtil.generateRandomString(5);
            password=PasswordUtil.encryptPassword(newPassword, salt);

            user.setPassword(password);
            user.setSalt(salt);
            user.setStatus(3);

            UserPVo userPVo = new UserPVo();
            userPVo.setUserEmail(user.getEmail());
            userPVo.setPassword(newPassword);
            userPVoList.add(userPVo);
        }

        vehicleUserService.updateBatchById(users);

        for (UserPVo userPVo : userPVoList) {
            xaiMessageFeignService.sendZhengShi(userPVo.getUserEmail(),userPVo.getPassword());
        }

        return R.ok().put("message", "File uploaded successfully!");
    }
}