package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehiclePlanDetailConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehiclePlanDetailEntity;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehiclePlanDetailService;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehiclePlanDetailQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehiclePlanDetailVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 方案细分表
*
* @author mumu
* @since 1.0.0 2025-04-16
*/
@RestController
@RequestMapping("test_platform/plan_detail")
@Tag(name="方案细分表")
@AllArgsConstructor
public class TestPlatformVehiclePlanDetailController {
    private final TestPlatformVehiclePlanDetailService testPlatformVehiclePlanDetailService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('test_platform:plan_detail:page')")
    public Result<PageResult<TestPlatformVehiclePlanDetailVO>> page(@ParameterObject @Valid TestPlatformVehiclePlanDetailQuery query){
        PageResult<TestPlatformVehiclePlanDetailVO> page = testPlatformVehiclePlanDetailService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('test_platform:plan_detail:info')")
    public Result<TestPlatformVehiclePlanDetailVO> get(@PathVariable("id") Long id){
        TestPlatformVehiclePlanDetailEntity entity = testPlatformVehiclePlanDetailService.getById(id);

        return Result.ok(TestPlatformVehiclePlanDetailConvert.INSTANCE.convert(entity));
    }

    @GetMapping("getList")
    @Operation(summary = "查询列表")
    @PreAuthorize("hasAuthority('test_platform:plan_detail:page') || hasAuthority('test_platform:test_case:page')")
    public Result<List<TestPlatformVehiclePlanDetailEntity>> getList(){
        List<TestPlatformVehiclePlanDetailEntity> list = testPlatformVehiclePlanDetailService.list();
        return Result.ok(list);
    }


    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('test_platform:plan_detail:save')")
    public Result<String> save(@RequestBody TestPlatformVehiclePlanDetailVO vo){
        testPlatformVehiclePlanDetailService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('test_platform:plan_detail:update')")
    public Result<String> update(@RequestBody @Valid TestPlatformVehiclePlanDetailVO vo){
        testPlatformVehiclePlanDetailService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('test_platform:plan_detail:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testPlatformVehiclePlanDetailService.delete(idList);

        return Result.ok();
    }
}