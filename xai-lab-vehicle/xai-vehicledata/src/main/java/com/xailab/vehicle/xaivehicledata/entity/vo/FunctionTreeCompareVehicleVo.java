package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionTreeCompareVehicleVo {

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
     * 车辆品牌名称
     */
    private String brandModel;

    /**
     * 车辆id
     */
    private String vehicleId;

    /**
     * 字幕Srt
     */
    private String srtUrl;
}
