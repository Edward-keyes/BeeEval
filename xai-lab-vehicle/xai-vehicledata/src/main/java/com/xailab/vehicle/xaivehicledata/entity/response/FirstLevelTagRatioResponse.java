package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

import java.util.List;

@Data
public class FirstLevelTagRatioResponse {

    private String oneTagName;

    private String oneId;

    private List<VehicleTagRatioResponse> vehicleTagRatio;
}
