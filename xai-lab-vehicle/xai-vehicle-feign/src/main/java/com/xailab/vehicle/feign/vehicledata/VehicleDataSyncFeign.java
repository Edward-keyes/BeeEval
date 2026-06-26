package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.feign.vo.DomainScoreSyncVO;
import com.xailab.vehicle.feign.vo.TertiaryMetricScoreSyncVO;
import com.xailab.vehicle.feign.vo.BasicAbilityScoreSyncVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 车辆数据同步Feign接口
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME, path = "/xai-vehicledata/sync", contextId = "vehicleDataSyncFeign")
public interface VehicleDataSyncFeign {

    /**
     * 同步功能域分数
     * 
     * @param syncData 同步数据
     * @return 同步结果
     */
    @PostMapping("/domainScore")
    Result<Void> syncDomainScore(@RequestBody List<DomainScoreSyncVO> syncData);

    /**
     * 同步三级指标分数
     * 
     * @param syncData 同步数据
     * @return 同步结果
     */
    @PostMapping("/tertiaryMetricScore")
    Result<Void> syncTertiaryMetricScore(@RequestBody List<TertiaryMetricScoreSyncVO> syncData);

    /**
     * 同步基础能力分数
     * 
     * @param syncData 同步数据
     * @return 同步结果
     */
    @PostMapping("/basicAbilityScore")
    Result<Void> syncBasicAbilityScore(@RequestBody List<BasicAbilityScoreSyncVO> syncData);

    /**
     * 同步开源题目分数（预留）
     * 
     * @param syncData 同步数据
     * @return 同步结果
     */
    /**
     * 获取BeeEval开源用例列表
     * 
     * @return 开源用例列表
     */
    @GetMapping("/openSourceCases")
    Result<List<Map<String, Object>>> getOpenSourceCases();

    /**
     * 同步开源题目分数
     * 
     * @param syncData 同步数据
     * @return 同步结果
     */
    @PostMapping("/openSourceScore")
    Result<Void> syncOpenSourceScore(@RequestBody List<Map<String, Object>> syncData);
}
