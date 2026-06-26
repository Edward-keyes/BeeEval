package com.xailab.vehicle.xaivehicledata.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.request.CaseRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.DomainTestCaseResponse;
import com.xailab.vehicle.xaivehicledata.service.DomainTestCaseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ware/domaintestcase")
public class DoMainTestCaseController {

    @Resource
    private DomainTestCaseService domainTestCaseService;

    @RequestMapping("/input")
    public Result input(@RequestPart("path") String path) {

        System.out.println(path);

        domainTestCaseService.domainTestCaseDataInput(path);

        return Result.ok();
    }

    @SaCheckLogin
    @PostMapping("/getCaseByVehicleIdAndDomainIndexId")
    public Result<List<DomainTestCaseResponse>> getCaseByVehicleIdAndDomainIndexId(@RequestBody CaseRequest caseRequest) {

        List<DomainTestCaseResponse> domainTestCaseResponses = domainTestCaseService.queryCaseByVehicleIdAndDomainIndexId(caseRequest);

        return Result.ok(domainTestCaseResponses);
    }

}