package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.TestProcessOptionsConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessOptionsEntity;
import com.xailab.vehicle.operation.testplatform.service.TestProcessOptionsService;
import com.xailab.vehicle.operation.testplatform.query.TestProcessOptionsQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessOptionsVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 流程选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/test_process_options")
@Tag(name="流程选项表")
@AllArgsConstructor
public class TestProcessOptionsController {
    private final TestProcessOptionsService testProcessOptionsService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:test_process_options:page')")
    public Result<PageResult<TestProcessOptionsVO>> page(@ParameterObject @Valid TestProcessOptionsQuery query){
        PageResult<TestProcessOptionsVO> page = testProcessOptionsService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:test_process_options:info')")
    public Result<TestProcessOptionsVO> get(@PathVariable("id") Long id){
        TestProcessOptionsEntity entity = testProcessOptionsService.getById(id);

        return Result.ok(TestProcessOptionsConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:test_process_options:save')")
    public Result<String> save(@RequestBody TestProcessOptionsVO vo){
        testProcessOptionsService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:test_process_options:update')")
    public Result<String> update(@RequestBody @Valid TestProcessOptionsVO vo){
        testProcessOptionsService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:test_process_options:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testProcessOptionsService.delete(idList);

        return Result.ok();
    }
}