package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FunctionDomainConvertVo {
    @JsonProperty("rowNum")
    private Integer rowNum;
    @JsonProperty("功能域")
    private String functionDomainIdS;
    @JsonProperty("三级指标")
    private String functionDomainIndexIdS;
    @JsonProperty("标题")
    private String title;
    @JsonProperty("任务类型")
    private String taskType;
    @JsonProperty("描述")
    private String description;
    @JsonProperty("视频文件名")
    private String videoName;
    @JsonProperty("title")
    private String titleEn;
    @JsonProperty("description")
    private String descriptionEn;
}
