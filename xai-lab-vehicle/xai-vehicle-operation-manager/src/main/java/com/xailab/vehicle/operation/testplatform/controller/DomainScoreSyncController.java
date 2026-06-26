package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.service.DomainScoreSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 功能域分数同步控制器
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@RestController
@RequestMapping("testplatform/sync")
@Tag(name = "功能域分数同步")
@AllArgsConstructor
@Slf4j
public class DomainScoreSyncController {

    private final DomainScoreSyncService domainScoreSyncService;

    /**
     * 同步功能域分数加权均分
     */
    @PostMapping("/domainScore")
    @Operation(summary = "同步功能域分数")
//    @PreAuthorize("hasAuthority('testplatform:sync:domain_score')")
    public Result syncDomainScore(@RequestParam("testRecordId") Integer testRecordId,
                                       @RequestParam("vehicleBaseInfoId") Long vehicleBaseInfoId) {
        log.info("接收到同步功能域分数请求: testRecordId={}, vehicleBaseInfoId={}", testRecordId, vehicleBaseInfoId);
        return domainScoreSyncService.syncDomainScore(testRecordId, vehicleBaseInfoId);
    }

    /**
     * 同步功能域下三级指标均分
     */
    @PostMapping("/tertiaryMetricScore")
    @Operation(summary = "同步三级指标分数")
//    @PreAuthorize("hasAuthority('testplatform:sync:tertiary_metric_score')")
    public Result syncTertiaryMetricScore(@RequestParam("testRecordId") Integer testRecordId,
                                               @RequestParam("vehicleBaseInfoId") Long vehicleBaseInfoId) {
        log.info("接收到同步三级指标分数请求: testRecordId={}, vehicleBaseInfoId={}", testRecordId, vehicleBaseInfoId);
        return domainScoreSyncService.syncTertiaryMetricScore(testRecordId, vehicleBaseInfoId);
    }

    /**
     * 同步基础能力指标分数
     */
    @PostMapping("/basicAbilityScore")
    @Operation(summary = "同步基础能力分数")
//    @PreAuthorize("hasAuthority('testplatform:sync:basic_ability_score')")
    public Result syncBasicAbilityScore(@RequestParam("testRecordId") Integer testRecordId,
                                             @RequestParam("vehicleBaseInfoId") Long vehicleBaseInfoId) {
        log.info("接收到同步基础能力分数请求: testRecordId={}, vehicleBaseInfoId={}", testRecordId, vehicleBaseInfoId);
        return domainScoreSyncService.syncBasicAbilityScore(testRecordId, vehicleBaseInfoId);
    }

    /**
     * 同步开源题目分数
     */
    @PostMapping("/openSourceScore")
    @Operation(summary = "同步开源题目分数")
//    @PreAuthorize("hasAuthority('testplatform:sync:open_source_score')")
    public Result syncOpenSourceScore(@RequestParam("testRecordId") Integer testRecordId,
                                           @RequestParam("vehicleBaseInfoId") Long vehicleBaseInfoId) {
        log.info("接收到同步开源题目分数请求: testRecordId={}, vehicleBaseInfoId={}", testRecordId, vehicleBaseInfoId);
        return domainScoreSyncService.syncOpenSourceScore(testRecordId, vehicleBaseInfoId);
    }
}
