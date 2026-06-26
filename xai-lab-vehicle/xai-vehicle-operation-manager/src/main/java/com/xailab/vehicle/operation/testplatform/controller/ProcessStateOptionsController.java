package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.ProcessStateOptionsConvert;
import com.xailab.vehicle.operation.testplatform.entity.ProcessStateOptionsEntity;
import com.xailab.vehicle.operation.testplatform.service.ProcessStateOptionsService;
import com.xailab.vehicle.operation.testplatform.query.ProcessStateOptionsQuery;
import com.xailab.vehicle.operation.testplatform.vo.ProcessStateOptionsVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 流程多选项 选择结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/process_state_options")
@Tag(name="流程多选项 选择结果")
@AllArgsConstructor
public class ProcessStateOptionsController {
    private final ProcessStateOptionsService processStateOptionsService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:process_state_options:page')")
    public Result<PageResult<ProcessStateOptionsVO>> page(@ParameterObject @Valid ProcessStateOptionsQuery query){
        PageResult<ProcessStateOptionsVO> page = processStateOptionsService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:process_state_options:info')")
    public Result<ProcessStateOptionsVO> get(@PathVariable("id") Long id){
        ProcessStateOptionsEntity entity = processStateOptionsService.getById(id);

        return Result.ok(ProcessStateOptionsConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:process_state_options:save')")
    public Result<String> save(@RequestBody ProcessStateOptionsVO vo){
        processStateOptionsService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:process_state_options:update')")
    public Result<String> update(@RequestBody @Valid ProcessStateOptionsVO vo){
        processStateOptionsService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:process_state_options:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        processStateOptionsService.delete(idList);

        return Result.ok();
    }
}