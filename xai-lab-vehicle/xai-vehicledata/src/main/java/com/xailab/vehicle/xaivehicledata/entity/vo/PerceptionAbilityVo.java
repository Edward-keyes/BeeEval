package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class PerceptionAbilityVo {

    /**
     * 二级指标名称
     */
    private String perceptionAbilityName;

    /**
     * 三级指标名称
     */
    private String threeTagName;

    /**
     * 车辆ID
     */
    private String vehicleId;

    /**
     * 车辆名称
     */
    private String vehicleName;

    /**
     * 是否有该能力
     * 0：无
     * 2：有
     */
    private String isHave;

}
