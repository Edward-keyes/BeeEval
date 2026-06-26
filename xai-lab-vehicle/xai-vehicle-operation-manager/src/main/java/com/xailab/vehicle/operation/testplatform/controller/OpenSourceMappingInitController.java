package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.operation.testplatform.service.OpenSourceMappingInitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开源用例关联映射初始化控制器
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@RestController
@RequestMapping("testplatform/mapping")
@Tag(name = "开源用例关联映射初始化")
@AllArgsConstructor
@Slf4j
public class OpenSourceMappingInitController {

    private final OpenSourceMappingInitService openSourceMappingInitService;

    /**
     * 初始化测试用例与开源用例的关联关系
     */
    @PostMapping("/initOpenSourceMapping")
    @Operation(summary = "初始化开源用例关联映射")
//    @PreAuthorize("hasAuthority('testplatform:mapping:init')")
    public Result initOpenSourceMapping() {
        log.info("接收到初始化开源用例关联映射请求");
        return openSourceMappingInitService.initOpenSourceMapping();
    }
}
