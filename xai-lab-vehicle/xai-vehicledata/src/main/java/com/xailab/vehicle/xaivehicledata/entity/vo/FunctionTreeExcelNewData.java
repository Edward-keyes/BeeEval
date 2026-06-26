package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FunctionTreeExcelNewData {

    /**
     * 测试日期
     */
    @JsonProperty("测试时间")
    private String test_date;
    /**
     * 功能标注
     */
    @JsonProperty("功能标注")
    private String label_explain;
    /**
     * 图片视频编号
     */
    @JsonProperty("视频编号")
    private String video_picture_number;
    /**
     * 车辆系统版本
     */
    @JsonProperty("系统版本")
    private String vehicle_system_version;
    /**
     * 车辆品牌
     */
    private String brand;
    /**
     * 车辆型号
     */
    @JsonProperty("车型")
    private String vehicle_model;
    /**
     * 功能说明
     */
    @JsonProperty("功能说明")
    private String function_label;
    /**
     * 三级指标
     */
    @JsonProperty("三级指标")
    private String function_three_tag;
    /**
     * 功能清单
     */
    @JsonProperty("功能清单")
    private String function_list;
}