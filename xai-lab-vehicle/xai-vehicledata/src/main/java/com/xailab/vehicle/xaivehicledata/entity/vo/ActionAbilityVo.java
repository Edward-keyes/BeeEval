package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class ActionAbilityVo {

    /**
     * 车辆id
     */

    private String vehicleId;

    /**
     * 车辆名称
     */
    private String vehicleName;

    /**
     * 行动能力数值
     */
    private Double actionNumber;

    /**
     * 行动能力描述
     */
    private String actionDescription;

    private String actionDescriptionId;

    /**
     * 单位
     */
    private String unit;
}
