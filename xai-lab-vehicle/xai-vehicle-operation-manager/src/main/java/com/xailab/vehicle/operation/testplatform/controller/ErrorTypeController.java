package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.ErrorTypeConvert;
import com.xailab.vehicle.operation.testplatform.entity.ErrorTypeEntity;
import com.xailab.vehicle.operation.testplatform.service.ErrorTypeService;
import com.xailab.vehicle.operation.testplatform.query.ErrorTypeQuery;
import com.xailab.vehicle.operation.testplatform.vo.ErrorTypeVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/error_type")
@Tag(name="")
@AllArgsConstructor
public class ErrorTypeController {
    private final ErrorTypeService errorTypeService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:error_type:page')")
    public Result<PageResult<ErrorTypeVO>> page(@ParameterObject @Valid ErrorTypeQuery query){
        PageResult<ErrorTypeVO> page = errorTypeService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:error_type:info')")
    public Result<ErrorTypeVO> get(@PathVariable("id") Long id){
        ErrorTypeEntity entity = errorTypeService.getById(id);

        return Result.ok(ErrorTypeConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:error_type:save')")
    public Result<String> save(@RequestBody ErrorTypeVO vo){
        errorTypeService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:error_type:update')")
    public Result<String> update(@RequestBody @Valid ErrorTypeVO vo){
        errorTypeService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:error_type:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        errorTypeService.delete(idList);

        return Result.ok();
    }
}