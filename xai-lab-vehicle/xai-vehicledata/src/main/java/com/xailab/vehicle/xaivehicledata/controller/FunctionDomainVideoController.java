package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.service.FunctionDomainVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ware/functiondomainvideo")
public class FunctionDomainVideoController {

    @Autowired
    FunctionDomainVideoService functionDomainVideoService;

    @PostMapping("uploadVideoAndPicture")
    public Result<String> upload(@RequestParam("path") String path) {

        functionDomainVideoService.uploadVideoAndPicture(path);

        return Result.ok("success");
    }

}
