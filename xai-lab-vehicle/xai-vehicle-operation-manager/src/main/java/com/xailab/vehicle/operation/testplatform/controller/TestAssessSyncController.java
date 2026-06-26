package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.service.TestAssessSyncService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testAssessSync")
public class TestAssessSyncController {

    @Resource
    TestAssessSyncService testAssessSyncService;

    //自动根据数据名称对应匹配关联
    @PostMapping("sync")
    @PreAuthorize("hasAuthority('test_platform:test_assess_sync:sync')")
    public Result sync(){
        testAssessSyncService.defaultSync();
        return Result.ok();

    }

}