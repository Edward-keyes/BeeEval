package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.FunctionTreeStateConvert;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeStateEntity;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreeStateService;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeStateQuery;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeStateVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 功能树状态表
*
* @author mumu 
* @since 1.0.0 2025-05-18
*/
@RestController
@RequestMapping("maku/function_tree_state")
@Tag(name="功能树状态表")
@AllArgsConstructor
public class FunctionTreeStateController {
    private final FunctionTreeStateService functionTreeStateService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:function_tree_state:page')")
    public Result<PageResult<FunctionTreeStateVO>> page(@ParameterObject @Valid FunctionTreeStateQuery query){
        PageResult<FunctionTreeStateVO> page = functionTreeStateService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:function_tree_state:info')")
    public Result<FunctionTreeStateVO> get(@PathVariable("id") Long id){
        FunctionTreeStateEntity entity = functionTreeStateService.getById(id);

        return Result.ok(FunctionTreeStateConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:function_tree_state:save')")
    public Result<String> save(@RequestBody FunctionTreeStateVO vo){
        functionTreeStateService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:function_tree_state:update')")
    public Result<String> update(@RequestBody @Valid FunctionTreeStateVO vo){
        functionTreeStateService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:function_tree_state:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        functionTreeStateService.delete(idList);

        return Result.ok();
    }
}