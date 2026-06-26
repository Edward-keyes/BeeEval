package com.xailab.vehicle.operation.testplatform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xailab.vehicle.operation.testplatform.pojo.request.QueryStateDataVehicleIdRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.StateDataResponse;
import com.xailab.vehicle.operation.testplatform.vo.ScoreIdVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleTestStateConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestStateService;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestStateQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleTestStateVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 测试记录用例表
*
* @author mumu 
* @since 1.0.0 2025-04-16
*/
@RestController
@RequestMapping("test_platform/test_state")
@Tag(name="测试记录用例表")
@AllArgsConstructor
public class TestPlatformVehicleTestStateController {
    private final TestPlatformVehicleTestStateService testPlatformVehicleTestStateService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('test_platform:test_state:page')")
    public Result<PageResult<TestPlatformVehicleTestStateVO>> page(@ParameterObject @Valid TestPlatformVehicleTestStateQuery query){
        PageResult<TestPlatformVehicleTestStateVO> page = testPlatformVehicleTestStateService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('test_platform:test_state:info')")
    public Result<TestPlatformVehicleTestStateVO> get(@PathVariable("id") Long id){
        TestPlatformVehicleTestStateEntity entity = testPlatformVehicleTestStateService.getById(id);

        return Result.ok(TestPlatformVehicleTestStateConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('test_platform:test_state:save')")
    public Result<String> save(@RequestBody TestPlatformVehicleTestStateVO vo){
        testPlatformVehicleTestStateService.save(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('test_platform:test_state:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testPlatformVehicleTestStateService.delete(idList);

        return Result.ok();
    }

    @PostMapping("/update")
    @Operation(summary = "更新")
    @PreAuthorize("hasAuthority('test_platform:test_state:update')")
    public Result<String> update(@RequestBody List<ScoreIdVo> id){

        testPlatformVehicleTestStateService.updateScore(id);

        return Result.ok("修改成功");
    }

    @PostMapping("/queryStateData")
    @PreAuthorize("hasAuthority('test_platform:test_state:queryStateData')")
    public Result<IPage<StateDataResponse>> queryStateData(@RequestBody QueryStateDataVehicleIdRequest request){
        IPage<StateDataResponse> stateDataResponseIPage = testPlatformVehicleTestStateService.selectTestStatePage(request.getPageNum(), request.getPageSize(), request.getVehicleId(),request);

        return Result.ok(stateDataResponseIPage);
    }
}