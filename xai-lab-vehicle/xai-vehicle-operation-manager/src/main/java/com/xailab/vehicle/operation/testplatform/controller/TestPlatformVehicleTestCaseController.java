package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.framework.operatelog.annotations.OperateLog;
import com.xailab.vehicle.framework.operatelog.enums.OperateTypeEnum;
import com.xailab.vehicle.operation.system.utils.poi.ExcelUtils;
import com.xailab.vehicle.operation.testplatform.pojo.excel.TestCaseBathAddImportTemplate;
import com.xailab.vehicle.operation.testplatform.pojo.response.TestPlatformImportByExcelResultResponse;
import com.xailab.vehicle.operation.testplatform.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestCaseService;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestCaseQuery;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* 测试用例总表
*
* @author mumu 
* @since 1.0.0 2025-04-16
*/
@RestController
@RequestMapping("/test_platform/testCase")
@Tag(name="测试用例总表")
@AllArgsConstructor
public class TestPlatformVehicleTestCaseController {
    private final TestPlatformVehicleTestCaseService testPlatformVehicleTestCaseService;

    /**
     * 分页查询测试用例
     * @param query
     * @return
     */
    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('test_platform:test_case:page')")
    public Result<PageResult<TestPlatformVehicleTestCaseVO>> page(@ParameterObject @Valid TestPlatformVehicleTestCaseQuery query){
        PageResult<TestPlatformVehicleTestCaseVO> page = testPlatformVehicleTestCaseService.page(query);

        return Result.ok(page);
    }

    /**
     * 查询测试用例下的 每台车的评分情况
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @Operation(summary = "查询测试用例下的评分详情")
    @PreAuthorize("hasAuthority('test_platform:test_case:info')")
    public Result<List<TestPlatformTestStateQueryResVo>> get(@PathVariable("id") Integer id){
        List<TestPlatformTestStateQueryResVo> entity = testPlatformVehicleTestCaseService.findTestStateInfo(id);
        return Result.ok(entity);
    }

    /**
     * 查询测试用例下的三级指标树
     * @return
     */
    @GetMapping("/getIndexTree")
    @Operation(summary = "查询测试用例下的三级指标树")
    @PreAuthorize("hasAuthority('test_platform:test_case:info')")
    public Result<List<TestPlatformTestCaseTreeVo>> getIndexTree(){
        List<TestPlatformTestCaseTreeVo> entity = testPlatformVehicleTestCaseService.findTestCasIndexTree();
        return Result.ok(entity);
    }


    /**
     * 查询场景List
     * @return
     */
    @GetMapping("/getScenarioList")
    @Operation(summary = "查询场景List")
    @PreAuthorize("hasAuthority('test_platform:test_case:info')")
    public Result<List<TestPlatformTestScenarioResVo>> getScenarioList(){
        List<TestPlatformTestScenarioResVo> entity = testPlatformVehicleTestCaseService.findTestScenarioList();
        return Result.ok(entity);
    }


    /**
     * 新增
     * @param vo
     * @return
     */
    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('test_platform:test_case:save')")
    public Result<String> save(@RequestBody @Valid TestPlatformVehicleTestCaseAddVO vo){
        testPlatformVehicleTestCaseService.save(vo);

        return Result.ok();
    }

    /**
     * 修改
     * @param vo
     * @return
     */
    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('test_platform:test_case:update')")
    public Result<String> update(@RequestBody @Valid TestPlatformVehicleTestCaseUpdateVO vo){
        testPlatformVehicleTestCaseService.update(vo);

        return Result.ok();
    }

    /**
     * 删除
     * @param idList
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('test_platform:test_case:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        testPlatformVehicleTestCaseService.delete(idList);
        return Result.ok();
    }

    /**
     * 批量excel导入
     * @param file
     */
    @PostMapping("/importByExcel")
    @Operation(summary = "测试用例数据批量导入")
    @OperateLog(type = OperateTypeEnum.IMPORT)
    @PreAuthorize("hasAuthority('test_platform:test_case:save')")
    public Result<TestPlatformImportByExcelResultResponse> importByExcel(@RequestPart("file") MultipartFile file){
        TestPlatformImportByExcelResultResponse response = testPlatformVehicleTestCaseService.importByExcel(file);
        return Result.ok(response);
    }

    /**
     * 批量excel导入模板下载
     */
    @PostMapping("importExcelTemplate")
    @Operation(summary = "测试用例数据批量导入模板")
    @OperateLog(type = OperateTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('test_platform:test_case:save')")
    public void importByExcelTemplate(HttpServletResponse response){
        ExcelUtils.exportTemplate(response,"测试用例批量导入模板",TestCaseBathAddImportTemplate.class,true);
    }

    /**
     * 同步测评题库id
     */
    @PostMapping("/queryRelevancySynchronization")
    public void queryRelevancySynchronization(){
        testPlatformVehicleTestCaseService.queryRelevancySynchronization();
    }

    /**
     * 基于车辆id将开源题目同步(镜像)
     */
    @GetMapping("/queryOpenSourceSynchronization")
    public void queryOpenSourceSynchronization(@RequestParam String recordId,@RequestParam String beeevalVehicleId){
        testPlatformVehicleTestCaseService.queryOpenSourceSynchronization(recordId,beeevalVehicleId);
    }
}