package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

@Data
public class TertiaryMetricWeightResponse {

    /**
     * 车辆名称
     */
    private String vehicleName;

    /**
     * 功能域
     */
    private String planDetailName;

    /**
     * 三级指标
     */
    private String tertiaryMetric;

    /**
     * 题目数量
     */
    private Integer count;

    /**
     * 平均分
     */
    private Double avgScore;

    /**
     * 权重
     */
    private Double weight;

    /**
     * 加权得分
     */
    private Double weightedScore;

}
