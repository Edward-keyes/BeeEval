package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

@Data
public class ActionAbilityResponse {

    /**
     * 行动能力数值
     */
    private Double actionNumber;

    /**
     * 行动能力描述
     */
    private String actionDescription;

    /**
     * 单位
     */
    private String unit;

}
