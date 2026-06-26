package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.FunctionTreeSyncTaskInfoConvert;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskInfoEntity;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreeSyncTaskInfoService;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeSyncTaskInfoQuery;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncTaskInfoVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 功能树同步任务详情表
*
* @author mm 
* @since 1.0.0 2025-06-02
*/
@RestController
@RequestMapping("testplatform/function_tree_sync_task_info")
@Tag(name="功能树同步任务详情表")
@AllArgsConstructor
public class FunctionTreeSyncTaskInfoController {
    private final FunctionTreeSyncTaskInfoService functionTreeSyncTaskInfoService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('testplatform:function_tree_sync_task_info:page')")
    public Result<PageResult<FunctionTreeSyncTaskInfoVO>> page(@ParameterObject @Valid FunctionTreeSyncTaskInfoQuery query){
        PageResult<FunctionTreeSyncTaskInfoVO> page = functionTreeSyncTaskInfoService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('testplatform:function_tree_sync_task_info:info')")
    public Result<FunctionTreeSyncTaskInfoVO> get(@PathVariable("id") Long id){
        FunctionTreeSyncTaskInfoEntity entity = functionTreeSyncTaskInfoService.getById(id);

        return Result.ok(FunctionTreeSyncTaskInfoConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('testplatform:function_tree_sync_task_info:save')")
    public Result<String> save(@RequestBody FunctionTreeSyncTaskInfoVO vo){
        functionTreeSyncTaskInfoService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('testplatform:function_tree_sync_task_info:update')")
    public Result<String> update(@RequestBody @Valid FunctionTreeSyncTaskInfoVO vo){
        functionTreeSyncTaskInfoService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('testplatform:function_tree_sync_task_info:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        functionTreeSyncTaskInfoService.delete(idList);

        return Result.ok();
    }
}