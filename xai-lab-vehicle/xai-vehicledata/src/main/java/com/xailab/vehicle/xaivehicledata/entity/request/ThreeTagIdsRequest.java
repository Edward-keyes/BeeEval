package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class ThreeTagIdsRequest {

    /**
     * 三级标签ID
     */
    private String threeTagIds;

    /**
     * 车辆ID
     */
    private String vehicleIds;

    private String language;

}
