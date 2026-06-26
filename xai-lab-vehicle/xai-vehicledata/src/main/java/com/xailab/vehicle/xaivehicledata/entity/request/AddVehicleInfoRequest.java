package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.Date;

@Data
public class AddVehicleInfoRequest {

    /**
     * 品牌名称
     */
    private String brand;

    /**
     * 品牌英文名称
     */
    private String brand_en;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 车型版本
     */
    private String vehicleVersion;

    /**
     * 车型版本英文
     */
    private String vehicleVersionEn;

    /**
     * 上市时间
     */
    private Date timeToMarket;

    /**
     * 测试时间
     */
    private Date testDate;

    /**
     * 车辆系统版本
     */
    private String vehicleSystemVersion;

    /**
     * 车载模型名称
     */
    private String modelName;

    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 能源类型En
     */
    private String energyTypeEn;

    /**
     * 纯电续航里程
     */
    private String enduranceMileage;

    /**
     * 创建时间
     */
    private Date createDate=new Date();

    /**
     * 更新时间
     */
    private Date updateDate=new Date();

    /**
     * 状态
     * 1=开放
     * 2=敬请期待
     */
    private Integer status;

    /**
     * 是否删除
     * 0=未删除
     * 1=删除
     */
    private Integer isDelete;
}
