package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.util.List;

@Data
public class FunctionTreeSynchronizationVoRequest {

    String taskName;

    String description;

    Long updater;

    List<FunctionTreeSynchronizationRequest> functionTreeSynchronizationRequest;

}
