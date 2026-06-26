package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.TestQuesOptionsConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestQuesOptionsEntity;
import com.xailab.vehicle.operation.testplatform.service.TestQuesOptionsService;
import com.xailab.vehicle.operation.testplatform.query.TestQuesOptionsQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesOptionsVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 功能评价选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/test_ques_options")
@Tag(name="功能评价选项表")
@AllArgsConstructor
public class TestQuesOptionsController {
    private final TestQuesOptionsService testQuesOptionsService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:test_ques_options:page')")
    public Result<PageResult<TestQuesOptionsVO>> page(@ParameterObject @Valid TestQuesOptionsQuery query){
        PageResult<TestQuesOptionsVO> page = testQuesOptionsService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:test_ques_options:info')")
    public Result<TestQuesOptionsVO> get(@PathVariable("id") Long id){
        TestQuesOptionsEntity entity = testQuesOptionsService.getById(id);

        return Result.ok(TestQuesOptionsConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:test_ques_options:save')")
    public Result<String> save(@RequestBody TestQuesOptionsVO vo){
        testQuesOptionsService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:test_ques_options:update')")
    public Result<String> update(@RequestBody @Valid TestQuesOptionsVO vo){
        testQuesOptionsService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:test_ques_options:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testQuesOptionsService.delete(idList);

        return Result.ok();
    }
}