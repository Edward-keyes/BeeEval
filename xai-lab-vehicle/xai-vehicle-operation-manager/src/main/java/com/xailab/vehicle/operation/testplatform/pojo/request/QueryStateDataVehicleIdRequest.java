package com.xailab.vehicle.operation.testplatform.pojo.request;

import lombok.Data;

import java.util.List;

@Data
public class QueryStateDataVehicleIdRequest {

    private List<Integer> vehicleId;

    private Integer pageNum;

    private Integer pageSize;

    private String testcaseContent;

    private Integer isSuccessful;

    private List<Integer> score;

    private Integer testStatus;

    private String functionName;

    private Integer caseType;

    private Integer errorType;

    private Integer caseId;

    private String threeTag;

    private String errorDetail;
}
