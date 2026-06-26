package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.xailab.vehicle.xaivehicledata.entity.response.FunctionalDomainRepresentation;
import lombok.Data;

import java.util.List;

@Data
public class FunctionalDomainVehicleVo {

    /**
     * 车辆id
     */
    private String vehicleId;

    /**
     * 车辆名称
     */
    private String vehicleName;

    /**
     * 功能域
     */
    private List<FunctionalDomainRepresentation> functionalDomains;

}
