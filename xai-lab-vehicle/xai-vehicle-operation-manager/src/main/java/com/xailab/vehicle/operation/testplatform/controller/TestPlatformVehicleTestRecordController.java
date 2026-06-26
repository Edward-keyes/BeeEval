package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.operation.testplatform.vo.TestPlatformTestRecordVehicleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleTestRecordConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestRecordEntity;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestRecordService;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestRecordQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleTestRecordVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* test_record
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@RestController
@RequestMapping("test_platform/test_record")
@Tag(name="test_record")
@AllArgsConstructor
public class TestPlatformVehicleTestRecordController {
    private final TestPlatformVehicleTestRecordService testPlatformVehicleTestRecordService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('test_platform:test_record:page')")
    public Result<PageResult<TestPlatformVehicleTestRecordVO>> page(@ParameterObject @Valid TestPlatformVehicleTestRecordQuery query){
        PageResult<TestPlatformVehicleTestRecordVO> page = testPlatformVehicleTestRecordService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('test_platform:test_record:info')")
    public Result<TestPlatformVehicleTestRecordVO> get(@PathVariable("id") Long id){
        TestPlatformVehicleTestRecordEntity entity = testPlatformVehicleTestRecordService.getById(id);

        return Result.ok(TestPlatformVehicleTestRecordConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('test_platform:test_record:save')")
    public Result<String> save(@RequestBody TestPlatformVehicleTestRecordVO vo){
        testPlatformVehicleTestRecordService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('test_platform:test_record:update')")
    public Result<String> update(@RequestBody @Valid TestPlatformVehicleTestRecordVO vo){
        testPlatformVehicleTestRecordService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('test_platform:test_record:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testPlatformVehicleTestRecordService.delete(idList);

        return Result.ok();
    }

    @GetMapping("list")
    @Operation(summary = "查询全部")
    @PreAuthorize("hasAuthority('test_platform:test_record:page') || hasAuthority('test_platform:funciton_tree:page') ||  hasAuthority('testplatform:sync_task:page')")
    public Result<List<TestPlatformTestRecordVehicleVO>> page(){
        List<TestPlatformTestRecordVehicleVO> list = testPlatformVehicleTestRecordService.findAll();
        return Result.ok(list);
    }
}