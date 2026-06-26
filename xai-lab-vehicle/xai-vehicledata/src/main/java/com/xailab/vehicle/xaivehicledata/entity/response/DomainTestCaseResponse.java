package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

@Data
public class DomainTestCaseResponse {

    private Integer score;

    private String testInstruct;

    private String url;

    private String interpretationOfResult;

    private String indexName;

    private String vehicleId;

    private String vehicleName;

    private String detail;

}
