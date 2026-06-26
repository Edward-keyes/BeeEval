package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleInfoConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleInfoEntity;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleInfoService;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleInfoQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleInfoVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 车辆信息表
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@RestController
@RequestMapping("test_platform/vehicle")
@Tag(name="车辆信息表")
@AllArgsConstructor
public class TestPlatformVehicleInfoController {
    private final TestPlatformVehicleInfoService testPlatformVehicleInfoService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('test_platform:vehicle:page')")
    public Result<PageResult<TestPlatformVehicleInfoVO>> page(@ParameterObject @Valid TestPlatformVehicleInfoQuery query){
        PageResult<TestPlatformVehicleInfoVO> page = testPlatformVehicleInfoService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('test_platform:vehicle:info')")
    public Result<TestPlatformVehicleInfoVO> get(@PathVariable("id") Long id){
        TestPlatformVehicleInfoEntity entity = testPlatformVehicleInfoService.getById(id);

        return Result.ok(TestPlatformVehicleInfoConvert.INSTANCE.convert(entity));
    }

    @GetMapping("getVehicleList")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('test_platform:vehicle:page') || hasAuthority('test_platform:test_case:page')")
    public Result<List<TestPlatformVehicleInfoVO>> getVehicleList(){
        List<TestPlatformVehicleInfoVO> infoVOS = testPlatformVehicleInfoService.getVehicleList();
        return Result.ok(infoVOS);
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('test_platform:vehicle:save')")
    public Result<String> save(@RequestBody TestPlatformVehicleInfoVO vo){
        testPlatformVehicleInfoService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('test_platform:vehicle:update')")
    public Result<String> update(@RequestBody @Valid TestPlatformVehicleInfoVO vo){
        testPlatformVehicleInfoService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('test_platform:vehicle:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testPlatformVehicleInfoService.delete(idList);

        return Result.ok();
    }
}