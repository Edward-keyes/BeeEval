package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class GeneralAbilityVo {

    /**
     * 车辆id
     */

    private String vehicleId;

    /**
     * 车辆名称
     */
    private String vehicleName;

    /**
     * 通识能力描述
     */
    private String generalAbilityTag;

    /**
     * 通识能力数值
     */
    private Double generalAbilityValue;

}
