package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionTreeVideoVo {

    /**
     * 三级标签描述
     */
    private String description;

    /**
     * 车辆的三级标签功能介绍
     */
    private String functionLabel;

    /**
     * 视频编号
     */
    private String videoNumber;

    /**
     * 视频字幕
     */
    private Integer videoStr;

}
