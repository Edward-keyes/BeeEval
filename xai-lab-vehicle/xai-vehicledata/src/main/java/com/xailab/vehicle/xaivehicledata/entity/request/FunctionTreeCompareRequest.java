package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

@Data
public class FunctionTreeCompareRequest {

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
     * 车辆品牌名称
     */
    private String brandModel;

    /**
     * 图片URL
     */
    private String[] imageUrls;

    /**
     * 视频字幕
     */
    private String videoStr;
}
