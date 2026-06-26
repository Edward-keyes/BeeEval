package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncCreateRequest;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncTaskAuditRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.*;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncAuditJournalVO;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncSelectResponse;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncTaskVO;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeSyncTaskQuery;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskEntity;
import com.xailab.vehicle.operation.testplatform.vo.SyncTaskOperationJournalVO;

import java.util.List;

/**
 * 功能树数据同步表
 *
 * @author mumu 
 * @since 1.0.0 2025-06-02
 */
public interface FunctionTreeSyncTaskService extends BaseService<FunctionTreeSyncTaskEntity> {

    /**
     * 分页查询测试任务
     * @param query
     * @return
     */
    PageResult<FunctionTreeSyncTaskVO> page(FunctionTreeSyncTaskQuery query);


    /**
     * 查询审核记录分页列表
     */
    List<FunctionTreeSyncAuditJournalVO> findAuditJournalList(String taskSerial);

    /**
     * 查询function tree全部数据
     * @param testRecordId
     * @param syncRule
     * @return
     */
    @Deprecated
    List<FunctionTreeSyncTreeListResponse> findFunctionTreeList(Integer testRecordId,Integer syncRule,String taskSerial);


    /**
     * 查询 有映射关系的功能树结构数据
     * @param testRecordId
     * @param syncRule
     * @return
     */
    SyncTaskTreeResponse findMapTreeList(Integer testRecordId, Integer syncRule, String taskSerial);

    /**
     * 创建同步任务
     * @param request
     */
    void saveTask(FunctionTreeSyncCreateRequest request);

    /**
     * 编辑同步任务
     * @param request
     */
    void update(FunctionTreeSyncCreateRequest request);

    Result<Void> delete(List<Long> idList);


    /**
     * 同步任务镜像
     * @param taskSerial
     */
    Result<Void> syncTaskTest(String taskSerial);

    /**
     * 提交审核
     * @param taskSerial
     * @return
     */
    Result<Void> commitAudit(String taskSerial);

    /**
     * 审核同步任务
     * @param request
     */
    void auditSyncTask(FunctionTreeSyncTaskAuditRequest request);

    /**
     * 查询已经选中的功能树数据
     * @param taskSerial
     * @return
     */
    FunctionTreeSyncSelectResponse findSelectList(String taskSerial);

    /**
     * 任务同步信息回滚
     * @param taskSerial
     */
    Result<Void> taskSyncFallback(String taskSerial);

    /**
     * 查询同步任务操作日志
     * @param taskSerial
     * @return
     */
    Result<List<SyncTaskOperationJournalVO>> selectOperationJournal(String taskSerial);

    /**
     * 查询同步任务信息
     * @param taskSerial
     * @param functionTag
     * @return
     */
    FunctionTreeSyncTaskInfoResponse selectSyncTaskInfo(String taskSerial,String functionTag);

    /**
     * 编辑同步任务详细信息
     * @param request
     */
    void syncTaskInfoEdit(FunctionTreeSyncTaskInfoEditRequest request);

    /**
     * 查询同步任务信息
     * @param taskSerial
     * @param functionTag
     * @return
     */
    List<FunctionTreeSyncTaskCaseOptionResponse> selectSyncTaskCaseOption(String taskSerial,String functionTag);

    /**
     * 编辑同步任务用例选项详细信息
     * @param request
     */
    void syncTaskCaseOptionEdit(FunctionTreeSyncTaskCaseOptionEditRequest request);

    /**
     * 初始化同步任务用例选项数据
     */
    void initSyncTaskOption(String taskSerial);

}