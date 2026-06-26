package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.sql.Date;

@Data
public class VehicleInfoVo {

    /**
     * 车辆id
     */
    private String id;

    /**
     * 车辆品牌名称
     */
    private String brandModel;

    /**
     * 测试时间
     */
    private Date testDate;

    /**
     * 车辆版本
     */
    private String vehicleVersion;

    /**
     * 系统版本
     */
    private String vehicleSystemVersion;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 车辆类型
     */
    private Integer vehicleType;
}
