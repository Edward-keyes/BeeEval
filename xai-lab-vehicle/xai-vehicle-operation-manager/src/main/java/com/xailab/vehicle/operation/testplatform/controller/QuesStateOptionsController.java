package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.QuesStateOptionsConvert;
import com.xailab.vehicle.operation.testplatform.entity.QuesStateOptionsEntity;
import com.xailab.vehicle.operation.testplatform.service.QuesStateOptionsService;
import com.xailab.vehicle.operation.testplatform.query.QuesStateOptionsQuery;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateOptionsVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 问卷选项 选择结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/ques_state_options")
@Tag(name="问卷选项 选择结果")
@AllArgsConstructor
public class QuesStateOptionsController {
    private final QuesStateOptionsService quesStateOptionsService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:ques_state_options:page')")
    public Result<PageResult<QuesStateOptionsVO>> page(@ParameterObject @Valid QuesStateOptionsQuery query){
        PageResult<QuesStateOptionsVO> page = quesStateOptionsService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:ques_state_options:info')")
    public Result<QuesStateOptionsVO> get(@PathVariable("id") Long id){
        QuesStateOptionsEntity entity = quesStateOptionsService.getById(id);

        return Result.ok(QuesStateOptionsConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:ques_state_options:save')")
    public Result<String> save(@RequestBody QuesStateOptionsVO vo){
        quesStateOptionsService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:ques_state_options:update')")
    public Result<String> update(@RequestBody @Valid QuesStateOptionsVO vo){
        quesStateOptionsService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:ques_state_options:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        quesStateOptionsService.delete(idList);

        return Result.ok();
    }
}