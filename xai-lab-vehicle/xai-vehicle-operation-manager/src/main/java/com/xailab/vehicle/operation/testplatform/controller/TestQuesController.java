package com.xailab.vehicle.operation.testplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.TestQuesConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestQuesEntity;
import com.xailab.vehicle.operation.testplatform.service.TestQuesService;
import com.xailab.vehicle.operation.testplatform.query.TestQuesQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 功能评价数据表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@RestController
@RequestMapping("maku/test_ques")
@Tag(name="功能评价数据表")
@AllArgsConstructor
public class TestQuesController {
    private final TestQuesService testQuesService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('maku:test_ques:page')")
    public Result<PageResult<TestQuesVO>> page(@ParameterObject @Valid TestQuesQuery query){
        PageResult<TestQuesVO> page = testQuesService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('maku:test_ques:info')")
    public Result<TestQuesVO> get(@PathVariable("id") Long id){
        TestQuesEntity entity = testQuesService.getById(id);

        return Result.ok(TestQuesConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('maku:test_ques:save')")
    public Result<String> save(@RequestBody TestQuesVO vo){
        testQuesService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('maku:test_ques:update')")
    public Result<String> update(@RequestBody @Valid TestQuesVO vo){
        testQuesService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('maku:test_ques:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testQuesService.delete(idList);

        return Result.ok();
    }
}