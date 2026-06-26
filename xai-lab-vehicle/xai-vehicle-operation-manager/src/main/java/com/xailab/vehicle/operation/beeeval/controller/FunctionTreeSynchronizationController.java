package com.xailab.vehicle.operation.beeeval.controller;

import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationVoRequest;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.beeeval.entity.request.FunctionIdRequest;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationRequest;
import com.xailab.vehicle.operation.beeeval.entity.response.FunctionTreeSynchronizationResponse;
import com.xailab.vehicle.feign.vo.TestCaseByFunctionIdVo;
import com.xailab.vehicle.operation.beeeval.service.FunctionTreeSynchronizationService;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestCaseService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/functionTreeSynchronization")
@AllArgsConstructor
public class FunctionTreeSynchronizationController {

    @Resource
    FunctionTreeSynchronizationService functionTreeSynchronizationService;

    @Resource
    TestPlatformVehicleTestCaseService testPlatformVehicleTestCaseService;

    /**
     * pcafe功能id与beeeval功能树三级标签结构同步
     */
    @PostMapping("/functionTreeSynchronization")
    @PreAuthorize("hasAnyAuthority('beeeval:function_tree_synchronization:functionTreeSynchronization')")
    public Result functionTreeSynchronization(@RequestBody FunctionTreeSynchronizationVoRequest functionTreeSynchronizationVoRequest) {

        List<FunctionTreeSynchronizationRequest> functionTreeSynchronizationRequest
                = functionTreeSynchronizationVoRequest.getFunctionTreeSynchronizationRequest();

        List<TestCaseByFunctionIdVo> vos=testPlatformVehicleTestCaseService.caseQuery();

        Map<String, List<TestCaseByFunctionIdVo>> collect = vos.stream()
                .collect(Collectors.groupingBy(TestCaseByFunctionIdVo::getTaskDetail));

        for (FunctionTreeSynchronizationRequest request:functionTreeSynchronizationRequest) {

            request.setCaseInfoResponseList(collect.get(request.getFunctionTagName()));

        }

        functionTreeSynchronizationVoRequest.setFunctionTreeSynchronizationRequest(functionTreeSynchronizationRequest);

        Boolean b = functionTreeSynchronizationService.functionTreeSynchronization(functionTreeSynchronizationVoRequest);

        return Result.ok(b);
    }

    /**
     * 基于pcafe功能id查询用例
     */
    @PostMapping("/caseQueryByFunctionId")
    @PreAuthorize("handlerAdapter('beeeval:vehicle_info:caseQueryByFunctionId')")
    public Result caseQueryByFunctionId(@RequestBody FunctionIdRequest functionIdRequest) {

        List<TestCaseByFunctionIdVo> vos=testPlatformVehicleTestCaseService.caseQueryByFunctionId(functionIdRequest.getFunctionId());

        return Result.ok(vos);
    }

    /**
     * 查询所有pcafe功能id与beeeval功能树三级标签（已同步、pcafe功能id未同步、beeeval功能树三级标签未同步）
     */
    @PostMapping("/functionTreeSynchronizationQuery")
    @PreAuthorize("hasAuthority('beeeval:function_tree_synchronization:functionTreeSynchronizationQuery')")
    public Result<FunctionTreeSynchronizationResponse> functionTreeSynchronizationQuery() {

        FunctionTreeSynchronizationResponse ftsr = functionTreeSynchronizationService.functionTreeSynchronizationQuery();

        return Result.ok(ftsr);
    }

    /**
     * 初始化已关联标签的用例数据同步至beeeval
     */
    @PostMapping("/initCaseDataSynchronization")
//    @PreAuthorize("hasAuthority('beeeval:function_tree_synchronization:initCaseDataSynchronization')")
    public Result initCaseDataSynchronization() {

        functionTreeSynchronizationService.initCaseDataSynchronization();

        return Result.ok();
    }
}
