package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.feign.vo.BasicAbilityScoreSyncVO;
import com.xailab.vehicle.feign.vo.DomainScoreSyncVO;
import com.xailab.vehicle.feign.vo.TertiaryMetricScoreSyncVO;
import com.xailab.vehicle.xaivehicledata.service.DataSyncService;
import com.xailab.vehicle.xaicommon.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据同步控制器
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@RestController
@RequestMapping("/sync")
@Slf4j
public class DataSyncController {

    @Autowired
    private DataSyncService dataSyncService;

    /**
     * 同步功能域分数
     */
    @PostMapping("/domainScore")
    public Result<Void> syncDomainScore(@RequestBody List<DomainScoreSyncVO> syncData) {
        log.info("接收到功能域分数同步请求，数据条数: {}", syncData.size());
        try {
            dataSyncService.syncDomainScore(syncData);
            return Result.ok();
        } catch (Exception e) {
            log.error("同步功能域分数失败", e);
            return Result.error("同步功能域分数失败: " + e.getMessage());
        }
    }

    /**
     * 同步三级指标分数
     */
    @PostMapping("/tertiaryMetricScore")
    public Result<Void> syncTertiaryMetricScore(@RequestBody List<TertiaryMetricScoreSyncVO> syncData) {
        log.info("接收到三级指标分数同步请求，数据条数: {}", syncData.size());
        try {
            dataSyncService.syncTertiaryMetricScore(syncData);
            return Result.ok();
        } catch (Exception e) {
            log.error("同步三级指标分数失败", e);
            return Result.error("同步三级指标分数失败: " + e.getMessage());
        }
    }

    /**
     * 同步基础能力分数
     */
    @PostMapping("/basicAbilityScore")
    public Result<Void> syncBasicAbilityScore(@RequestBody List<BasicAbilityScoreSyncVO> syncData) {
        log.info("接收到基础能力分数同步请求，数据条数: {}", syncData.size());
        try {
            dataSyncService.syncBasicAbilityScore(syncData);
            return Result.ok();
        } catch (Exception e) {
            log.error("同步基础能力分数失败", e);
            return Result.error("同步基础能力分数失败: " + e.getMessage());
        }
    }

    /**
     * 获取BeeEval开源用例列表
     */
    @GetMapping("/openSourceCases")
    public Result<List<Map<String, Object>>> getOpenSourceCases() {
        log.info("接收到获取开源用例列表请求");
        try {
            List<Map<String, Object>> cases = dataSyncService.getOpenSourceCases();
            return Result.ok(cases);
        } catch (Exception e) {
            log.error("获取开源用例列表失败", e);
            return Result.error("获取开源用例列表失败: " + e.getMessage());
        }
    }

    /**
     * 同步开源题目分数
     */
    @PostMapping("/openSourceScore")
    public Result<Void> syncOpenSourceScore(@RequestBody List<Map<String, Object>> syncData) {
        log.info("接收到开源题目分数同步请求，数据条数: {}", syncData.size());
        try {
            dataSyncService.syncOpenSourceScore(syncData);
            return Result.ok();
        } catch (Exception e) {
            log.error("同步开源题目分数失败", e);
            return Result.error("同步开源题目分数失败: " + e.getMessage());
        }
    }
}
