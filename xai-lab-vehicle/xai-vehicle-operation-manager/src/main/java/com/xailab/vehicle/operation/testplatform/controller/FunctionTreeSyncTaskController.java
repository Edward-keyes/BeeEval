package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.feign.vo.FunctionTreeSyncTaskVo;
import com.xailab.vehicle.framework.operatelog.annotations.OperateLog;
import com.xailab.vehicle.framework.operatelog.enums.OperateTypeEnum;
import com.xailab.vehicle.operation.system.service.SysUserService;
import com.xailab.vehicle.operation.system.vo.SysUserVO;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncCreateRequest;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncTaskAuditRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.*;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncAuditJournalVO;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncSelectResponse;
import com.xailab.vehicle.operation.testplatform.vo.SyncTaskOperationJournalVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.convert.FunctionTreeSyncTaskConvert;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskEntity;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreeSyncTaskService;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeSyncTaskQuery;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
* 功能树数据同步表
*
* @author mumu
* @since 1.0.0 2025-06-02
*/
@RestController
@RequestMapping("testplatform/sync_task")
@Tag(name="功能树数据同步表")
@AllArgsConstructor
@Slf4j
public class FunctionTreeSyncTaskController {
    private final FunctionTreeSyncTaskService functionTreeSyncTaskService;

    @Resource
    private SysUserService sysUserService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @OperateLog(module = "功能树数据同步",name = "分页", type = OperateTypeEnum.GET)
    @PreAuthorize("hasAuthority('testplatform:sync_task:page')")
    public Result<PageResult<FunctionTreeSyncTaskVO>> page(@ParameterObject @Valid FunctionTreeSyncTaskQuery query){
        PageResult<FunctionTreeSyncTaskVO> page = functionTreeSyncTaskService.page(query);

        return Result.ok(page);
    }


    /**
     * 查询 有映射关系的功能树结构数据
     * @param testRecordId
     * @param syncRule
     * @return
     */
    @GetMapping("/findMapTreeList")
    @Operation(summary = "查询功能树结构")
//    @OperateLog(module = "功能树数据同步",name = "查询功能树结构", type = OperateTypeEnum.GET)
    @PreAuthorize("hasAuthority('testplatform:sync_task:info')")
    public Result<SyncTaskTreeResponse> findMapTreeList(@RequestParam("testRecordId") Integer testRecordId,
                                                @RequestParam("syncRule") Integer syncRule,
                                                @RequestParam(value = "taskSerial",  required = false) String taskSerial) {
        SyncTaskTreeResponse mapTreeList = functionTreeSyncTaskService.findMapTreeList(testRecordId, syncRule, taskSerial);
        return Result.ok(mapTreeList);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('testplatform:sync_task:info')")
    public Result<FunctionTreeSyncTaskVO> get(@PathVariable("id") Long id) {
        FunctionTreeSyncTaskEntity entity = functionTreeSyncTaskService.getById(id);
        Map<Long, SysUserVO> userMap = sysUserService.getUserMap(Stream.of(entity.getCreator(), entity.getUpdater()).toList());
        FunctionTreeSyncTaskVO functionTreeSyncTaskVO = FunctionTreeSyncTaskConvert.INSTANCE.convert(entity);
        functionTreeSyncTaskVO.setCreatorName(userMap.get(entity.getCreator()).getUsername());
        functionTreeSyncTaskVO.setUpdaterName(userMap.get(entity.getUpdater()).getUsername());
        return Result.ok(functionTreeSyncTaskVO);
    }

    @PostMapping("/saveTask")
    @Operation(summary = "保存")
    @OperateLog(module = "功能树数据同步",name = "保存", type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('testplatform:sync_task:save')")
    public Result<String> saveTask(@RequestBody @Valid FunctionTreeSyncCreateRequest vo){
        functionTreeSyncTaskService.saveTask(vo);
        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(module = "功能树数据同步",name = "修改", type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('testplatform:sync_task:update')")
    public Result<String> update(@RequestBody @Valid FunctionTreeSyncCreateRequest vo){
        functionTreeSyncTaskService.update(vo);
        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(module = "功能树数据同步",name = "删除", type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('testplatform:sync_task:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        functionTreeSyncTaskService.delete(idList);

        return Result.ok();
    }


    /**
     * 审核同步任务
     * @param request
     */
    @PostMapping("/auditSyncTask")
    @Operation(summary = "同步任务审核")
//    @OperateLog(module = "功能树数据同步",name = "同步任务审核", type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('testplatform:sync_task:audit')")
    public Result<String> auditSyncTask(@RequestBody FunctionTreeSyncTaskAuditRequest request){
        functionTreeSyncTaskService.auditSyncTask(request);
        return Result.ok();
    }

    /**
     * 查询已经选中的功能树数据
     * @param taskSerial
     * @return
     */
    @GetMapping("/findSelectList")
    @Operation(summary = "查询已经选中的数据")
//    @OperateLog(module = "功能树数据同步",name = "查询已经选中的数据", type = OperateTypeEnum.GET)
    @PreAuthorize("hasAuthority('testplatform:sync_task:info')")
    public Result<FunctionTreeSyncSelectResponse> findSelectList(@RequestParam("taskSerial") String taskSerial){
        FunctionTreeSyncSelectResponse selectList = functionTreeSyncTaskService.findSelectList(taskSerial);
        return Result.ok(selectList);
    }
    /**
     * 查询审核记录
     */
    @GetMapping("/findAuditJournalList")
    @Operation(summary = "查询审核记录")
//    @OperateLog(module = "功能树数据同步",name = "查询审核记录", type = OperateTypeEnum.GET)
    @PreAuthorize("hasAuthority('testplatform:sync_task:info')")
    public Result<List<FunctionTreeSyncAuditJournalVO>> findAuditJournalList(@RequestParam("taskSerial") String taskSerial){
        List<FunctionTreeSyncAuditJournalVO> auditJournalList = functionTreeSyncTaskService.findAuditJournalList(taskSerial);
        return Result.ok(auditJournalList);
    }

    /**
     * 存储审核任务
     */
    @PostMapping("/saveAuditTask")
    @Operation(summary = "存储审核任务")
    @OperateLog(module = "功能树数据同步",name = "存储审核任务", type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('testplatform:sync_task:update')")
    public Result<Boolean> saveAuditTask(@RequestBody FunctionTreeSyncTaskVo request){
        FunctionTreeSyncTaskEntity convert = FunctionTreeSyncTaskConvert.INSTANCE.convert(request);
        return Result.ok(functionTreeSyncTaskService.save(convert));
    }

    /**
     * 同步任务镜像
     * @param taskSerial
     */
    @PostMapping("/syncTaskTest")
    @Operation(summary = "同步任务镜像")
    @OperateLog(module = "功能树数据同步",name = "同步任务镜像", type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('testplatform:sync_task:update')")
    public com.xailab.vehicle.feign.common.Result<Void> syncTaskTest(@RequestParam(name = "taskSerial") String taskSerial){
        return functionTreeSyncTaskService.syncTaskTest(taskSerial);
    }

    /**
     * 提交审核
     * @param taskSerial
     * @return
     */
    @PostMapping("/commitAudit")
    @Operation(summary = "提交审核")
    @OperateLog(module = "功能树数据同步",name = "提交审核", type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('testplatform:sync_task:update')")
    public Result<Void> commitAudit(@RequestParam(name = "taskSerial") String taskSerial){
        functionTreeSyncTaskService.commitAudit(taskSerial);
        return Result.ok();
    }


    /**
     * 查询同步任务操作日志
     * @param taskSerial
     * @return
     */
    @GetMapping("/operationJournal")
//    @OperateLog(module = "功能树数据同步",name = "查询同步任务操作日志", type = OperateTypeEnum.GET)
    @PreAuthorize("hasAuthority('testplatform:sync_task:info')")
    public com.xailab.vehicle.feign.common.Result<List<SyncTaskOperationJournalVO>> selectOperationJournal(@RequestParam(name = "taskSerial") String taskSerial){
        return functionTreeSyncTaskService.selectOperationJournal(taskSerial);
    }


    /**
     * 查询同步任务详情信息
     *
     * @param taskSerial
     * @param functionTag
     * @return
     */
    @GetMapping("/selectSyncTaskInfo")
//    @OperateLog(module = "功能树数据同步",name = "查询同步任务详情信息", type = OperateTypeEnum.GET)
    @PreAuthorize("hasAuthority('testplatform:sync_task:info')")
    public Result<FunctionTreeSyncTaskInfoResponse> selectSyncTaskInfo(@RequestParam(name = "taskSerial") String taskSerial,
                                                               @RequestParam(name = "functionTag")String functionTag) {
        FunctionTreeSyncTaskInfoResponse functionTreeSyncTaskInfoResponse = functionTreeSyncTaskService.selectSyncTaskInfo(taskSerial, functionTag);
        return Result.ok(functionTreeSyncTaskInfoResponse);
    }

    /**
     * 编辑同步任务详细信息
     *
     * @param request
     */
    @PostMapping("/syncTaskInfoEdit")
    @OperateLog(module = "功能树数据同步",name = "编辑同步任务详细信息", type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('testplatform:sync_task:update')")
    public Result<Void> syncTaskInfoEdit(@RequestBody FunctionTreeSyncTaskInfoEditRequest request) {
        log.info("编辑同步任务详细信息：{}", request);
        functionTreeSyncTaskService.syncTaskInfoEdit(request);
        return Result.ok();
    }


    /**
     * 编辑同步任务用例选项详细信息
     * @param request
     * @return
     */
    @PostMapping("/syncTaskCaseOptionEdit")
    @OperateLog(module = "功能树数据同步",name = "编辑同步任务用例选项信息", type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('testplatform:sync_task:update')")
    public Result<Void> syncTaskCaseOptionEdit(@RequestBody @Valid FunctionTreeSyncTaskCaseOptionEditRequest request) {
        log.info("编辑同步任务用例选项信息：{}", request);
        functionTreeSyncTaskService.syncTaskCaseOptionEdit(request);
        return Result.ok();
    }

    /**
     * 查询同步任务信息
     * @param taskSerial
     * @param functionTag
     * @return
     */
    @GetMapping("/selectSyncTaskCaseOption")
    @OperateLog(module = "功能树数据同步",name = "查询同步任务信息", type = OperateTypeEnum.GET)
    @PreAuthorize("hasAuthority('testplatform:sync_task:info')")
    public Result<List<FunctionTreeSyncTaskCaseOptionResponse>> selectSyncTaskCaseOption(@RequestParam(name = "taskSerial") String taskSerial,
                                                                          @RequestParam(name = "functionTag")String functionTag){
        log.info("查询同步任务信息：{}", taskSerial);
        return Result.ok(functionTreeSyncTaskService.selectSyncTaskCaseOption(taskSerial, functionTag));
    }


    /**
     * 初始化同步任务用例选项数据
     */
    @PostMapping("/initSyncTaskOption")
    public Result<Void> initSyncTaskOption(@RequestParam(name = "taskSerial", required = false) String taskSerial){
        log.info("初始化同步任务用例选项数据：{}", taskSerial);
        functionTreeSyncTaskService.initSyncTaskOption(taskSerial);
        return Result.ok();
    }



}