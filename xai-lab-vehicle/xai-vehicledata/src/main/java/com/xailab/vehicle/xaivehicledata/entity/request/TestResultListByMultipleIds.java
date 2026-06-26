package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class TestResultListByMultipleIds {

    private List<String> vehicleId;

    private String functionIndexId;

    private String language;

}
