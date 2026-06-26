package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.feign.vo.BasicAbilityScoreSyncVO;
import com.xailab.vehicle.feign.vo.DomainScoreSyncVO;
import com.xailab.vehicle.feign.vo.TertiaryMetricScoreSyncVO;

import java.util.List;
import java.util.Map;

/**
 * 数据同步服务接口
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
public interface DataSyncService {

    /**
     * 同步功能域分数
     * @param syncData 同步数据
     */
    void syncDomainScore(List<DomainScoreSyncVO> syncData);

    /**
     * 同步三级指标分数
     * @param syncData 同步数据
     */
    void syncTertiaryMetricScore(List<TertiaryMetricScoreSyncVO> syncData);

    /**
     * 同步基础能力分数
     * @param syncData 同步数据
     */
    void syncBasicAbilityScore(List<BasicAbilityScoreSyncVO> syncData);

    /**
     * 获取BeeEval开源用例列表
     * @return 开源用例列表
     */
    List<Map<String, Object>> getOpenSourceCases();

    /**
     * 同步开源题目分数
     * @param syncData 同步数据
     */
    void syncOpenSourceScore(List<Map<String, Object>> syncData);
}
