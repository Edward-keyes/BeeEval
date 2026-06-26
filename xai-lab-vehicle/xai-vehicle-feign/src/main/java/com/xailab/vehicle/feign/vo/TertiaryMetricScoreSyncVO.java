package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 三级指标分数同步VO
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Data
public class TertiaryMetricScoreSyncVO {

    /**
     * BeeEval车辆基础信息ID
     */
    private Long vehicleBaseInfoId;

    /**
     * 功能域ID
     */
    private Long functionalDomainId;

    /**
     * 功能域名称
     */
    private String functionalDomainName;

    /**
     * 三级指标ID
     */
    private Long domainIndexId;

    /**
     * 三级指标名称
     */
    private String indexName;

    /**
     * 三级指标分数
     */
    private BigDecimal indexScore;

    /**
     * 测试记录ID（用于关联）
     */
    private Integer testRecordId;
}
