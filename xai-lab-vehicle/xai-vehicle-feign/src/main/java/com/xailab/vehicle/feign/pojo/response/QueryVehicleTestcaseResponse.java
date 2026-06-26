package com.xailab.vehicle.feign.pojo.response;

import lombok.Data;

@Data
public class QueryVehicleTestcaseResponse {

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
     * 测试问题
     */
    private String testcaseContent;

    /**
     * 分
     */
    private Integer score;

    /**
     * 分数id
     */
    private Integer scoreId;

    /**
     * 用例id
     */
    private Integer caseId;

    /**
     * 状态 0：失败 1：成功
     */
    private Integer isSuccessful;
}
