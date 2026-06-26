package com.xailab.vehicle.xaivehicledata.feign;

import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaicommon.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "xai-message" , path = "/xai-message")
public interface XaiMessageFeignService {

    @RequestMapping("/test/hello")
    public R hello();

    @RequestMapping("/test/send")
    public Result send(@RequestParam("email") String email, @RequestParam("password") String password);

    @RequestMapping("/test/sendEn")
    public Result sendEn(@RequestParam("email") String email, @RequestParam("password") String password);

    @RequestMapping("/test/sendJp")
    public Result sendJp(@RequestParam("email") String email, @RequestParam("password") String password);

    @RequestMapping("/test/sendZhengShi")
    public Result sendZhengShi(@RequestParam("email") String email, @RequestParam("password") String password);

    @RequestMapping("/test/sendZhengShiTest")
    public Result sendZhengShiTest(@RequestParam("email") String email, @RequestParam("password") String password);


}
