package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 基础能力分数同步VO
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Data
public class BasicAbilityScoreSyncVO {

    /**
     * BeeEval车辆基础信息ID
     */
    private Long vehicleBaseInfoId;

    /**
     * 能力类型：1-认知能力，2-行动能力
     */
    private Integer abilityType;

    /**
     * 能力类型名称
     */
    private String abilityTypeName;

    /**
     * 指标名称
     */
    private String indexName;

    /**
     * 指标分数
     */
    private BigDecimal indexScore;

    /**
     * 单位
     */
    private String unit;

    /**
     * 测试记录ID（用于关联）
     */
    private Integer testRecordId;
}
