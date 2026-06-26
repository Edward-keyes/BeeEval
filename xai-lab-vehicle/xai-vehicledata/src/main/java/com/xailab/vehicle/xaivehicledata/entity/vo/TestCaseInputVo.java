package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseInputVo {

    @JsonProperty("品牌-车型")
    private String brandName;

    @JsonProperty("系统版本")
    private String systemVersion;

    @JsonProperty("测试时间")
    private String testTime;

    @JsonProperty("功能域")
    private String functionDomain;

    @JsonProperty("三级指标")
    private String threeLevelIndex;

    @JsonProperty("用例")
    private String testcase;

    @JsonProperty("评分")
    private String score;

    @JsonProperty("评分说明")
    private String scoreDescription;
}
