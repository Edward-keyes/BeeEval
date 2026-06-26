package com.xailab.vehicle.operation.beeeval.service;

import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationRequest;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationVoRequest;
import com.xailab.vehicle.operation.beeeval.entity.response.FunctionTreeSynchronizationResponse;

import java.util.List;

public interface FunctionTreeSynchronizationService {

    FunctionTreeSynchronizationResponse functionTreeSynchronizationQuery();

    Boolean functionTreeSynchronization(FunctionTreeSynchronizationVoRequest functionTreeSynchronizationRequest);

    void initCaseDataSynchronization();
}
