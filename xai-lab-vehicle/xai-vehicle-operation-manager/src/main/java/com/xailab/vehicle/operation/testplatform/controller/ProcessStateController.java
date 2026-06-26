package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.ProcessStateConvert;
import com.xailab.vehicle.operation.testplatform.entity.ProcessStateEntity;
import com.xailab.vehicle.operation.testplatform.service.ProcessStateService;
import com.xailab.vehicle.operation.testplatform.query.ProcessStateQuery;
import com.xailab.vehicle.operation.testplatform.vo.ProcessStateVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 流程状态结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/process_state")
@Tag(name="流程状态结果")
@AllArgsConstructor
public class ProcessStateController {
    private final ProcessStateService processStateService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:process_state:page')")
    public Result<PageResult<ProcessStateVO>> page(@ParameterObject @Valid ProcessStateQuery query){
        PageResult<ProcessStateVO> page = processStateService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:process_state:info')")
    public Result<ProcessStateVO> get(@PathVariable("id") Long id){
        ProcessStateEntity entity = processStateService.getById(id);

        return Result.ok(ProcessStateConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:process_state:save')")
    public Result<String> save(@RequestBody ProcessStateVO vo){
        processStateService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:process_state:update')")
    public Result<String> update(@RequestBody @Valid ProcessStateVO vo){
        processStateService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:process_state:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        processStateService.delete(idList);

        return Result.ok();
    }
}