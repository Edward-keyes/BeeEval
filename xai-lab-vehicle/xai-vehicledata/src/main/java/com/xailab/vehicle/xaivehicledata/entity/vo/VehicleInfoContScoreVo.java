package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class VehicleInfoContScoreVo {

    /**
     * 车辆详细信息id
     */
    private String id;

    /**
     * 系统版本
     */
    private String vehicleSystemVersion;

    /**
     * 测试时间
     */
    private Date testDate;

    /**
     * 车辆品牌名称 包含 型号
     */
    private String vehicleModelInfo;

    /**
     * 平均分数
     */
    private Double avgScore;
}
