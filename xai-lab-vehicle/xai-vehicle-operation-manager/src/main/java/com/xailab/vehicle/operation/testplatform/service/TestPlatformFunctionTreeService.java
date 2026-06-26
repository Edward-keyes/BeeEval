package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.pojo.request.*;
import com.xailab.vehicle.operation.testplatform.pojo.response.*;
import com.xailab.vehicle.operation.testplatform.vo.ErrorTypeVO;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeFirstTypeVO;

import java.util.List;

/**
 * 功能树
 */

public interface TestPlatformFunctionTreeService {

    /**
     * 功能树操作
     */

    /**
     * 功能树分页查询
     * @param request
     * @return
     */
    PageResult<TestPlatformFunctionTreeResponse> page(TestPlatformFunctionTreeRequest request);

    /**
     * 功能树新增
     */
    void add(FunctionTreeAddRequest request);


    /**
     * 功能状态编辑
     * @param request
     */
    void edit(FunctionTreeStateEditRequest request);

    /**
     * 查询详情
     * @param request
     * @return
     */
    List<FunctionTreeInfoResponse> queryInfo(FunctionTreeInfoRequest request);

    /**
     * 获取功能一级标签
     * @return
     */
    List<FunctionTreeLevelTypeResponse> queryTypeFirst();

    /**
     * 获取功能二级级标签
     * @return
     */
    List<FunctionTreeLevelTypeResponse> queryTypeSecond(String label);

    Result<Void> updateFunctionTreeState(Integer recordId, String taskDetail,Integer dataState);
    //同步状态
    Result<Void> updateFunctionTreeStateAsync(Integer recordId, String taskDetail,Integer dataState);


    /**
     * 功能树状态同步 ALL
     * @return
     */
    Result<Void> functionTreeStateSync();



    /**
     * 功能树测试用例 操作
     */

    /**
     * 测试用例详情查询
     * @param testCaseId
     * @return
     */
    FunctionTreeCaseProcessResponse findTestCaseProcess(Integer testCaseId);

    /**
     * 功能树测试用例选项 添加和编辑
     * @param response
     */
    void testCaseProcessEdit(FunctionTreeCaseProcessRequest response);

    /**
     * 测试用例添加
     * @param request
     */
    void testCaseAdd(FunctionTreeTestCaseAddRequest request);

    /**
     * 查询测试用例评价详情
     * @param testCaseId 测试用例id
     * @return
     */
    FunctionTreeCaseEvaluateResponse findCaseEvaluateInfo(Integer testCaseId);

    /**
     * 用例评价详情修改
     * @param request
     */
    void updateCaseEvaluate(FunctionTreeCaseEvaluateRequest request);


    /**
     * 查询测试用例功能走查详情
     * @param request
     * @return
     */
    List<FunctionTreeCaseWalkthroughResponse> findCaseWalkthroughInfo(FunctionTreeCaseWalkthroughRequest request);


    /**
     * 编辑用例功能走查详情
     * @param request
     */
    void updateCaseWalkthrough(List<FunctionTreeCaseWalkthroughResponse> request);





    /**
     * 测试数据
     */
    FunctionTreeCaseStateResponse testCaseStateQuery(FunctionTreeCaseStateRequest request);

    /**
     * 自定义数据转化为预设
     */
    void caseStateOptionSwitch(CaseStateOptionSwitchRequest request);

    /**
     * 测试用例状态选项编辑
     * @param request
     */
    void caseStateOptionEdit(CaseStateOptionEditRequest request);

    /**
     * 修改测试用例错误
     * @param request
     */
    void caseStateErrorTypeEdit(TestCaseStateErrorRequest request);

    /**
     * 测试状态编辑
     *
     * @param request
     */
    void testCaseStateEdit(TestCaseStateEditRequest request);

    /**
     * 查询对应问题的选项
     * @return
     */
    List<TestCaseOptionsInfoResponse> findOptions(TestCaseOptionsInfoRequest request);

    /**
     * 所有错误类型查询
     * @param caseType 0功能评价 1是功能走查
     * @return
     */
    List<ErrorTypeVO> findErrorType(Integer caseType);



    /**
     * 功能树评分相关
     */

    /**
     * 查询评分问题 选项题目等
     * @return
     */
    List<FunctionTreeScoreQuestionResponse> findScoreQuestion(String functionTag);

    /**
     * 评分问题编辑
     */
    FunctionTreeScoreQuestionResponse editScoreQuestion(FunctionTreeScoreQuestionEditRequest request);

    /**
     * 评分问题添加
     * @param request
     */
    void addScoreQuestion(FunctionTreeScoreQuestionAddRequest request);


    /**
     * 评分编辑和新增
     * @param request
     */
    void scoreQuestionUpdate(List<FunctionTreeScoreQuestionResponse> request);



}
