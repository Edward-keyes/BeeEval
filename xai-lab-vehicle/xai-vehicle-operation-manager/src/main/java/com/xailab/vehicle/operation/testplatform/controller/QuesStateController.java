package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.QuesStateConvert;
import com.xailab.vehicle.operation.testplatform.entity.QuesStateEntity;
import com.xailab.vehicle.operation.testplatform.service.QuesStateService;
import com.xailab.vehicle.operation.testplatform.query.QuesStateQuery;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 功能评价结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/ques_state")
@Tag(name="功能评价结果")
@AllArgsConstructor
public class QuesStateController {
    private final QuesStateService quesStateService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:ques_state:page')")
    public Result<PageResult<QuesStateVO>> page(@ParameterObject @Valid QuesStateQuery query){
        PageResult<QuesStateVO> page = quesStateService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:ques_state:info')")
    public Result<QuesStateVO> get(@PathVariable("id") Long id){
        QuesStateEntity entity = quesStateService.getById(id);

        return Result.ok(QuesStateConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:ques_state:save')")
    public Result<String> save(@RequestBody QuesStateVO vo){
        quesStateService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:ques_state:update')")
    public Result<String> update(@RequestBody @Valid QuesStateVO vo){
        quesStateService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:ques_state:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        quesStateService.delete(idList);

        return Result.ok();
    }
}