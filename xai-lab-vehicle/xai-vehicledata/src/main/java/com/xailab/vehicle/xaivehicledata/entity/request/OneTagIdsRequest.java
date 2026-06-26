package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class OneTagIdsRequest {

    /**
     * 一级标签ID列表
     */
    private List<String> oneTagIds;

    /**
     * 车辆ID列表
     */
    private List<String> vehicleIds;

    private String language;

}
