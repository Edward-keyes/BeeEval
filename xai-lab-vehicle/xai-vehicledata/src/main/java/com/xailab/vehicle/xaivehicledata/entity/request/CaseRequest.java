package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class CaseRequest {

    private String domainIndexId;

    private List<String> vehicleId;

    private String language;
}
