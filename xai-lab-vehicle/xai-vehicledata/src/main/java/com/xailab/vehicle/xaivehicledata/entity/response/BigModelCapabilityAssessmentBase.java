package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class BigModelCapabilityAssessmentBase {

    /**
     * 车辆id
     */

    private String vehicleId;

    /**
     * 车辆名称
     */
    private String vehicleName;

    /**
     * 车辆通识能力细项
     */

    private List<GeneralAbilityResponse> generalAbilities;

    /**
     * 车辆行动能力细项
     */
    private List<ActionAbilityResponse> actionAbilities;

}
