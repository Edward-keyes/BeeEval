package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

@Data
public class FunctionTreeVideoRequest {

    /**
     * 三级标签描述
     */
    private String description;

    /**
     * 车辆的三级标签功能介绍
     */
    private String[] functionLabel;

    /**
     * 视频编号
     */
    private String videoNumber;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频字幕
     */
    private String videoStr;

}
