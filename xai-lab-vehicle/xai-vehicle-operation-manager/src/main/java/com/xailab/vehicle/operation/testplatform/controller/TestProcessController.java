package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.TestProcessConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessEntity;
import com.xailab.vehicle.operation.testplatform.service.TestProcessService;
import com.xailab.vehicle.operation.testplatform.query.TestProcessQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 流程数据表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/test_process")
@Tag(name="流程数据表")
@AllArgsConstructor
public class TestProcessController {
    private final TestProcessService testProcessService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:test_process:page')")
    public Result<PageResult<TestProcessVO>> page(@ParameterObject @Valid TestProcessQuery query){
        PageResult<TestProcessVO> page = testProcessService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:test_process:info')")
    public Result<TestProcessVO> get(@PathVariable("id") Long id){
        TestProcessEntity entity = testProcessService.getById(id);

        return Result.ok(TestProcessConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:test_process:save')")
    public Result<String> save(@RequestBody TestProcessVO vo){
        testProcessService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:test_process:update')")
    public Result<String> update(@RequestBody @Valid TestProcessVO vo){
        testProcessService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:test_process:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testProcessService.delete(idList);

        return Result.ok();
    }
}