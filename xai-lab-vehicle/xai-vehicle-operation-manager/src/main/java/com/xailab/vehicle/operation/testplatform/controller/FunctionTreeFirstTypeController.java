package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.FunctionTreeFirstTypeConvert;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeFirstTypeEntity;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreeFirstTypeService;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeFirstTypeQuery;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeFirstTypeVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 功能树一级标签类型
*
* @author mu 
* @since  2025-05-31
*/
@RestController
@RequestMapping("testplatform/function_tree_first_type")
@Tag(name="功能树一级标签类型")
@AllArgsConstructor
public class FunctionTreeFirstTypeController {
    private final FunctionTreeFirstTypeService functionTreeFirstTypeService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('testplatform:function_tree_first_type:page')")
    public Result<PageResult<FunctionTreeFirstTypeVO>> page(@ParameterObject @Valid FunctionTreeFirstTypeQuery query){
        PageResult<FunctionTreeFirstTypeVO> page = functionTreeFirstTypeService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('testplatform:function_tree_first_type:info')")
    public Result<FunctionTreeFirstTypeVO> get(@PathVariable("id") Long id){
        FunctionTreeFirstTypeEntity entity = functionTreeFirstTypeService.getById(id);

        return Result.ok(FunctionTreeFirstTypeConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('testplatform:function_tree_first_type:save')")
    public Result<String> save(@RequestBody FunctionTreeFirstTypeVO vo){
        functionTreeFirstTypeService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('testplatform:function_tree_first_type:update')")
    public Result<String> update(@RequestBody @Valid FunctionTreeFirstTypeVO vo){
        functionTreeFirstTypeService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('testplatform:function_tree_first_type:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        functionTreeFirstTypeService.delete(idList);

        return Result.ok();
    }
}