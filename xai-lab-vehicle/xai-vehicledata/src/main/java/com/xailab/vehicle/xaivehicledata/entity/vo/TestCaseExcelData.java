package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseExcelData {

    /**
     * 测试日期
     */
    @JsonProperty("测试时间")
    private String test_date;

    /**
     * 车辆品牌
     */
    private String brand;
    /**
     * 车辆型号
     */
    @JsonProperty("品牌-车型")
    private String vehicle_model;

    /**
     * 车辆系统版本
     */
    @JsonProperty("系统版本")
    private String vehicle_system_version;

    /**
     * 功能域
     */
    @JsonProperty("功能域")
    private String function_domain;

    /**
     * 三级指标
     */
    @JsonProperty("三级指标")
    private String three_tag;

    /**
     * 用例
     */
    @JsonProperty("用例")
    private String test_case;

    /**
     * 评分
     */
    @JsonProperty("评分")
    private String score;

    /**
     * 评分说明
     */
    @JsonProperty("评分说明")
    private String score_explain;


}
