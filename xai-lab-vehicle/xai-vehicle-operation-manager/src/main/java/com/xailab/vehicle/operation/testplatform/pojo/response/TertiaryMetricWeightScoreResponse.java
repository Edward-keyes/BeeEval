package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

import java.util.List;

@Data
public class TertiaryMetricWeightScoreResponse {

//    private String vehicleName;
//
//    private String totalPoint;

    List<TertiaryMetricWeightResponse> tertiaryMetricWeightResponses;

    List<FunctionDomainScoreResponse> functionDomainScoreResponses;

}
