package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

@Data
public class QueryFunctionTreeVideoRequest {

    /**
     * 车辆id
     */
    private String vehicleId;

    /**
     * 三级标签id
     */
    private String threeTagId;

    private String language;

}
