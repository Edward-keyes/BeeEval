package com.xailab.vehicle.operation.testplatform.controller;

import java.util.List;

import com.xailab.vehicle.feign.pojo.request.VehicleIdRequest;
import com.xailab.vehicle.feign.pojo.response.QueryVehicleTestcaseResponse;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.pojo.request.EditBasicFunctionRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionDomainScoreResponse;
import com.xailab.vehicle.operation.testplatform.pojo.response.TertiaryMetricWeightScoreResponse;
import com.xailab.vehicle.feign.vo.VehicleIdBVIdRequest;
import com.xailab.vehicle.operation.testplatform.vo.BaseFunctionVo;
import com.xailab.vehicle.operation.testplatform.vo.VehicleIdsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.operation.testplatform.service.TertiaryMetricWeightService;



/**
 * 
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-22 17:05:43
 */
@RestController
@RequestMapping("testplatform/tertiarymetricweight")
public class TertiaryMetricWeightController {

    @Autowired
    private TertiaryMetricWeightService tertiaryMetricWeightService;

    /**
     * 基于车型id查询问题得分
     * @return
     */
    @PostMapping("queryTestcaseContentByVehicleId")
    @PreAuthorize("hasAuthority('test_platform:case_content:queryTestcaseContent')")
    public Result queryTestcaseContent(@RequestBody VehicleIdsRequest vehicleIdRequest){

        List<QueryVehicleTestcaseResponse> response = tertiaryMetricWeightService.queryTestcaseContentByVehicleId(vehicleIdRequest.getVehicleId());

        return Result.ok(response);
    }

    /**
     * 分数修改
     */

    /**
     * 基于车型id查询三级标签权重得分与功能域得分
     */
    @PostMapping("queryTertiaryMetricWeightByVehicleId")
    @PreAuthorize("hasAuthority('test_platform:case_content:queryTertiaryMetricWeight')")
    public Result queryTertiaryMetricWeight(@RequestBody VehicleIdsRequest vehicleIdRequest){

        TertiaryMetricWeightScoreResponse response = tertiaryMetricWeightService.queryTertiaryMetricWeight(vehicleIdRequest.getVehicleId());

        return Result.ok(response);

    }

    /**
     * 基于车型id查询基础功能得分
     */
    @PostMapping("queryBasicFunctionScoreByVehicleId")
    @PreAuthorize("hasAuthority('test_platform:case_content:queryBasicFunctionScore')")
    public Result queryBasicFunctionScore(@RequestBody VehicleIdsRequest vehicleIdRequest){
        List<BaseFunctionVo> response = tertiaryMetricWeightService.queryBasicFunctionScore(vehicleIdRequest.getVehicleId());
        return Result.ok(response);
    }

    /**
     * 编辑基础能力数据
     * （图像生成速度、文本生成速度、首字响应时长）
     */
    @PostMapping("editBasicFunctionScore")
    @PreAuthorize("hasAuthority('test_platform:case_content:editBasicFunctionScore')")
    public Result editBasicFunctionScore(@RequestBody EditBasicFunctionRequest editBasicFunctionRequests){

        Boolean ture=tertiaryMetricWeightService.editBasicFunctionScore(editBasicFunctionRequests);

        return Result.ok(ture);
    }

    /**
     * 基于相同功能域、相同指标下，通过中文进行绑定
     */
    //TODO 待完成
    @PostMapping("syncRule")
    @PreAuthorize("hasAuthority('test_platform:case_content:syncRule')")
    public Result syncRule(@RequestBody VehicleIdBVIdRequest vehicleIdRequest){

        Boolean b = tertiaryMetricWeightService.syncRule(vehicleIdRequest);

        return Result.ok();
    }

}
