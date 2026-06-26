package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.Result;

/**
 * 功能域分数同步服务接口
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
public interface DomainScoreSyncService {

    /**
     * 同步功能域分数加权均分
     * 
     * @param testRecordId      数据管理平台测试记录ID
     * @param vehicleBaseInfoId BeeEval车辆基础信息ID
     * @return 同步结果
     */
    Result syncDomainScore(Integer testRecordId, Long vehicleBaseInfoId);

    /**
     * 同步功能域下三级指标均分
     * 
     * @param testRecordId      数据管理平台测试记录ID
     * @param vehicleBaseInfoId BeeEval车辆基础信息ID
     * @return 同步结果
     */
    Result syncTertiaryMetricScore(Integer testRecordId, Long vehicleBaseInfoId);

    /**
     * 同步基础能力指标分数
     * 
     * @param testRecordId      数据管理平台测试记录ID
     * @param vehicleBaseInfoId BeeEval车辆基础信息ID
     * @return 同步结果
     */
    Result syncBasicAbilityScore(Integer testRecordId, Long vehicleBaseInfoId);

    /**
     * 同步开源题目分数（预留接口）
     * 
     * @param testRecordId      数据管理平台测试记录ID
     * @param vehicleBaseInfoId BeeEval车辆基础信息ID
     * @return 同步结果
     */
    Result syncOpenSourceScore(Integer testRecordId, Long vehicleBaseInfoId);
}
