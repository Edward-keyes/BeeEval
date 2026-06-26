package com.xailab.vehicle.operation.testplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.feign.pojo.response.QueryVehicleTestcaseResponse;
import com.xailab.vehicle.feign.vo.VehicleIdBVIdRequest;
import com.xailab.vehicle.operation.testplatform.entity.TertiaryMetricWeightEntity;
import com.xailab.vehicle.operation.testplatform.pojo.request.EditBasicFunctionRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionDomainScoreResponse;
import com.xailab.vehicle.operation.testplatform.pojo.response.TertiaryMetricWeightScoreResponse;
import com.xailab.vehicle.operation.testplatform.vo.BaseFunctionVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-22 17:05:43
 */
public interface TertiaryMetricWeightService extends IService<TertiaryMetricWeightEntity> {

    List<QueryVehicleTestcaseResponse> queryTestcaseContentByVehicleId(List<String> vehicleId);

    TertiaryMetricWeightScoreResponse queryTertiaryMetricWeight(List<String> vehicleId);

    List<BaseFunctionVo> queryTertiaryMetricBaseWeight(String vehicleId);

    Boolean syncRule(VehicleIdBVIdRequest vehicleIdRequest);

    List<BaseFunctionVo> queryBasicFunctionScore(List<String> vehicleId);

    Boolean editBasicFunctionScore(EditBasicFunctionRequest editBasicFunctionRequests);
}

