package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.service.PcafeRelevancyFunctionDomainService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testplatform/relevancy_function_domain")
@Tag(name="")
public class PcafeRelevancyFunctionDomainController {
    @Resource
    private PcafeRelevancyFunctionDomainService pcafeRelevancyFunctionDomainService;

    @PostMapping("/syncDomain")
    public Result syncDomain() {
        Boolean b = pcafeRelevancyFunctionDomainService.syncFunctionDomain();
        return Result.ok(b?"同步成功":"同步失败");
    }

}
