package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionalDomainIndicator {

    /**
     * 功能域指标ID
     */
    private String IndicatorId;

    /**
     * 功能域指标名称
     */
    private String IndicatorName;

    /**
     * 功能域指标值
     */
    private String IndicatorValue;

    /**
     * 测试案例名称
     */
    private String TestCaseName;

    /**
     * 测试指令
     */
    private String TestInstruction;

    /**
     * 响应截图
     */
    private String ResponseScreenshot;

    /**
     * 结果分析
     */
    private String ResultAnalysis;

}
