package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.sql.Date;

@Data
public class VehicleDetailInfoVo {

    /**
     * 车辆id
     */
    private String id;

    /**
     * 车辆品牌名称
     */
    private String vehicleName;

    /**
     * 车辆版本
     */
    private String vehicleVersion;

    /**
     * 车辆图片
     */
    private String vehiclePicture;

    /**
     * 系统版本
     */
    private String vehicleSystemVersion;

    /**
     * 更新时间
     */
    private Date testDate;

    /**
     * 上市时间
     */
    private Date timeToMarket;

    /**
     * 纯电续航里程
     */
    private String enduranceMileage;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 车辆状态 1-正常 2-未上线 3-试用车型
     */
    private Integer status;

    /**
     * 芯片信息
     */
    private String chipInformation;

    /**
     * 测试指标
     */
    private String testIndicators="56";

    /**
     * 测试用例
     */
    private String testCases="1200+";

    /**
     * 大模型功能数
     */
    private String bigModelFunctionCount="123";
}
