package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.framework.common.utils.DateUtils;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.framework.operatelog.annotations.OperateLog;
import com.xailab.vehicle.framework.operatelog.enums.OperateTypeEnum;
import com.xailab.vehicle.operation.testplatform.pojo.request.*;
import com.xailab.vehicle.operation.testplatform.pojo.response.*;
import com.xailab.vehicle.operation.testplatform.service.TestCaseMaterialService;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformFunctionTreeService;
import com.xailab.vehicle.operation.testplatform.task.TestCaseStateSyncTask;
import com.xailab.vehicle.operation.testplatform.service.UploadFileBatchService;
import com.xailab.vehicle.operation.testplatform.vo.ErrorTypeVO;
import com.xailab.vehicle.operation.testplatform.vo.TestCaseMaterialVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 功能树管理
 * @ClassName: TestPlatformFunctionTreeController
 * @Description:
 * @author: liulin
 * @date: 2025/5/5 17:09
 */
@RestController
@RequestMapping("/test_platform/funTree")
@Slf4j
public class TestPlatformFunctionTreeController {
    @Resource
    private TestPlatformFunctionTreeService testPlatformFunctionTreeService;

    @Resource
    private TestCaseMaterialService testCaseMaterialService;

    @Resource
    private UploadFileBatchService uploadFileBatchService;

    @Resource
    private TestCaseStateSyncTask testCaseStateSyncTask;

    /**
     * 功能树操作
     */

    /**
     * 功能树分页查询
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:page')")
    public Result<PageResult<TestPlatformFunctionTreeResponse>> page(@ParameterObject TestPlatformFunctionTreeRequest request) {
        log.info("功能树分页查询请求参数：{}",request);
        PageResult<TestPlatformFunctionTreeResponse> page = testPlatformFunctionTreeService.page(request);
        return Result.ok(page);
    }

    /**
     * 功能树新增
     */
    @PostMapping("/treeAdd")
    @Operation(summary = "功能树新增")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:add')")
    public Result<Void> add(@RequestBody @Valid FunctionTreeAddRequest request) {
        testPlatformFunctionTreeService.add(request);
        return Result.ok();
    }

    /**
     * 功能树修改
     */
    @PostMapping("/treeEdit")
    @Operation(summary = "功能树修改")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> edit(@RequestBody @Valid FunctionTreeStateEditRequest request) {
        testPlatformFunctionTreeService.edit(request);
        return Result.ok();
    }

    /**
     * 功能树详情
     */
    @PostMapping("/treeInfo")
    @Operation(summary = "功能树详情")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:info')")
    public Result<List<FunctionTreeInfoResponse>> info(@RequestBody @Valid FunctionTreeInfoRequest request) {
        return Result.ok(testPlatformFunctionTreeService.queryInfo(request));
    }


    /**
     * 获取功能一级标签
     * @return
     */
    @GetMapping("/treeTypeFirst")
    @Operation(summary = "功能树获取一级类型")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:info')")
    public Result<List<FunctionTreeLevelTypeResponse>> queryTypeFirst(){
        return Result.ok(testPlatformFunctionTreeService.queryTypeFirst());
    }

    /**
     * 获取功能二级级标签
     * @return
     */
    @GetMapping("/treeTypeSecond")
    @Operation(summary = "功能树获取二级类型")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:info')")
    public Result<List<FunctionTreeLevelTypeResponse>> queryTypeSecond(@RequestParam("label") String label){
        return Result.ok(testPlatformFunctionTreeService.queryTypeSecond(label));
    }


    /**
     * 功能树状态同步 ALL
     * @return
     */
    @PostMapping("/functionTreeStateSync")
    @Operation(summary = "功能树状态同步 ALL")
    public Result<Void> functionTreeStateSync(){
        return testPlatformFunctionTreeService.functionTreeStateSync();
    }


    /**
     * 功能树测试用例状态同步任务触发
     * @return
     */
    @PostMapping("/testCaseStateSyncTask")
    public Result<Void> syncTestCaseState(){
        testCaseStateSyncTask.syncTestCaseState();
        return Result.ok();
    }




    /**
     * 功能树测试用例 操作
     */

    /**
     * 测试用例详情查询
     *
     * @param testCaseId
     * @return
     */
    @PostMapping("/testCase/info/{testCaseId}")
    @Operation(summary = "测试用例详情查询")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<FunctionTreeCaseProcessResponse> findTestCaseProcess(@PathVariable("testCaseId") Integer testCaseId) {
        FunctionTreeCaseProcessResponse testCaseProcess = testPlatformFunctionTreeService.findTestCaseProcess(testCaseId);
        return Result.ok(testCaseProcess);
    }

    /**
     * 功能树测试用例详情 添加和编辑
     *
     * @param request
     */
    @PostMapping("/testCase/operation")
    @Operation(summary = "测试用例详情 添加和编辑")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> testCaseProcessEdit(@RequestBody @Valid FunctionTreeCaseProcessRequest request) {
        log.info("测试用例详情添加或编辑：{}",request);
        testPlatformFunctionTreeService.testCaseProcessEdit(request);
        return Result.ok();
    }

    /**
     * 测试用例添加
     *
     * @param request
     */
    @PostMapping("/testCase/add")
    @Operation(summary = "测试用例添加")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:add')")
    public Result<Void> testCaseAdd(@RequestBody @Valid FunctionTreeTestCaseAddRequest request) {
        log.info("测试用例添加：{}",request);
        testPlatformFunctionTreeService.testCaseAdd(request);
        return Result.ok();
    }



    /**
     * 查询测试用例评价详情
     * @param testCaseId 测试用例id
     * @return
     */
    @PostMapping("/testCase/evaluateInfo")
    @Operation(summary = "查询测试用例评价详情")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:info')")
    public Result<FunctionTreeCaseEvaluateResponse> findCaseEvaluateInfo(@RequestParam("testCaseId") Integer testCaseId){
        log.info("查询测试用例评价详情：{}",testCaseId);
        FunctionTreeCaseEvaluateResponse caseEvaluateInfo = testPlatformFunctionTreeService.findCaseEvaluateInfo(testCaseId);
        return Result.ok(caseEvaluateInfo);
    }

    /**
     * 用例评价详情修改
     * @param request
     */
    @PostMapping("/testCase/evaluateUpdate")
    @Operation(summary = "用例评价详情修改")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> updateCaseEvaluate(@RequestBody @Valid FunctionTreeCaseEvaluateRequest request){
        log.info("用例评价详情修改：{}",request);
        testPlatformFunctionTreeService.updateCaseEvaluate(request);
        return Result.ok();
    }


    /**
     * 查询测试用例功能走查详情
     * @param request
     * @return
     */
    @PostMapping("/testCase/walkthroughInfo")
    @Operation(summary = "查询测试用例功能走查详情")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:info')")
    public  Result<List<FunctionTreeCaseWalkthroughResponse>> findCaseWalkthroughInfo(@RequestBody @Valid FunctionTreeCaseWalkthroughRequest request){
        log.info("查询测试用例功能走查详情：{}",request);
        List<FunctionTreeCaseWalkthroughResponse> caseWalkthroughInfo = testPlatformFunctionTreeService.findCaseWalkthroughInfo(request);
        return Result.ok(caseWalkthroughInfo);
    }


    /**
     * 编辑用例功能走查详情
     * @param request
     */
    @PostMapping("/testCase/walkthroughUpdate")
    @Operation(summary = "编辑用例功能走查详情")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> updateCaseWalkthrough(@RequestBody List<FunctionTreeCaseWalkthroughResponse> request){
        log.info("编辑用例功能走查详情：{}",request);
        testPlatformFunctionTreeService.updateCaseWalkthrough(request);
        return Result.ok();
    }




    /**
     * 测试数据状态查询
     */
    @PostMapping("/testState/query")
    @Operation(summary = "测试数据状态查询")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:page')")
    public Result<FunctionTreeCaseStateResponse> testCaseStateQuery(@RequestBody @Valid FunctionTreeCaseStateRequest request) {
        log.info("测试数据状态查询：{}",request);
        FunctionTreeCaseStateResponse stateResponse = testPlatformFunctionTreeService.testCaseStateQuery(request);
        return Result.ok(stateResponse);
    }

    /**
     * 自定义数据转化为预设
     */
    @PostMapping("/testState/switch")
    @Operation(summary = "自定义数据转化为预设")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> caseStateOptionSwitch(@RequestBody @Valid CaseStateOptionSwitchRequest request) {
        testPlatformFunctionTreeService.caseStateOptionSwitch(request);
        return Result.ok();
    }

    /**
     * 测试用例状态选项编辑
     *
     * @param request
     */
    @PostMapping("/testState/edit")
    @Operation(summary = "测试用例状态选项编辑")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> caseStateOptionEdit(@RequestBody @Valid CaseStateOptionEditRequest request) {
        testPlatformFunctionTreeService.caseStateOptionEdit(request);
        return Result.ok();
    }

    /**
     * 修改测试用例错误
     *
     * @param request
     */
    @PostMapping("/testState/error/edit")
    @Operation(summary = "修改测试用例错误")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> caseStateErrorTypeEdit(@RequestBody @Valid TestCaseStateErrorRequest request) {
        testPlatformFunctionTreeService.caseStateErrorTypeEdit(request);
        return Result.ok();
    }

    /**
     * 测试状态编辑
     *
     * @param request
     */
    @PostMapping("/testState/state/edit")
    @Operation(summary = "测试状态编辑")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> testCaseStateEdit(@RequestBody @Valid TestCaseStateEditRequest request) {
        testPlatformFunctionTreeService.testCaseStateEdit(request);
        return Result.ok();
    }

    /**
     * 查询对应所有的选项内容
     *
     * @return
     */
    @PostMapping("/testState/state/findOptions")
    @Operation(summary = "查询对应所有的选项内容")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:page')")
    public Result<List<TestCaseOptionsInfoResponse>> findOptions(@RequestBody @Valid TestCaseOptionsInfoRequest request) {
        List<TestCaseOptionsInfoResponse> options = testPlatformFunctionTreeService.findOptions(request);
        return Result.ok(options);
    }

    /**
     * 所有错误类型查询
     *
     * @param caseType 0功能评价 1是功能走查
     * @return
     */
    @PostMapping("/testState/errorList/{caseType}")
    @Operation(summary = "所有错误类型查询")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:page')")
    public Result<List<ErrorTypeVO>> findErrorType(@PathVariable("caseType") Integer caseType) {
        List<ErrorTypeVO> errorType = testPlatformFunctionTreeService.findErrorType(caseType);
        return Result.ok(errorType);
    }


    /**
     * 功能树评分相关
     */

    /**
     * 查询评分问题 选项题目等
     *
     * @return
     */
    @PostMapping("/testCase/score/info")
    @Operation(summary = "查询评分问题 选项题目等")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<List<FunctionTreeScoreQuestionResponse>> findScoreQuestion(@RequestParam("functionTag") String functionTag) {
        List<FunctionTreeScoreQuestionResponse> scoreQuestion = testPlatformFunctionTreeService.findScoreQuestion(functionTag);
        return Result.ok(scoreQuestion);
    }

    /**
     * 评分问题编辑
     */
    @PostMapping("/testCase/score/edit")
    @Operation(summary = "评分问题编辑")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<FunctionTreeScoreQuestionResponse> editScoreQuestion(@RequestBody @Valid FunctionTreeScoreQuestionEditRequest request) {
        FunctionTreeScoreQuestionResponse functionTreeScoreQuestionResponse = testPlatformFunctionTreeService.editScoreQuestion(request);
        return Result.ok(functionTreeScoreQuestionResponse);
    }

    /**
     * 评分问题添加
     *
     * @param request
     */
    @PostMapping("/testCase/score/add")
    @Operation(summary = "评分问题添加")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> addScoreQuestion(@RequestBody @Valid FunctionTreeScoreQuestionAddRequest request) {
        testPlatformFunctionTreeService.addScoreQuestion(request);
        return Result.ok();
    }


    /**
     * 评分编辑和新增
     * @param request
     */
    @PostMapping("/testCase/score/addOrEdit")
    @Operation(summary = "评分编辑和新增")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> scoreQuestionUpdate(@RequestBody List<FunctionTreeScoreQuestionResponse> request){
        testPlatformFunctionTreeService.scoreQuestionUpdate(request);
        return Result.ok();
    }


    /**
     * 文件素材管理
     */
    /**
     * 上传文件
     * @param file
     * @param recordId
     * @param testCaseId
     * @return
     */
    @PostMapping("/testCase/material/upload")
    @Operation(summary = "用例文件素材上传")
    @OperateLog(type = OperateTypeEnum.OTHER)
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:upload')")
    public Result<String> uploadFile(@RequestPart("file") MultipartFile file,
                                     @RequestParam("recordId") Integer recordId,
                                     @RequestParam("testCaseId") Integer testCaseId){
        return Result.ok(testCaseMaterialService.uploadFile(file,recordId,testCaseId));
    }

    /**
     * 批量视频上传
     */
    @PostMapping("/testCase/material/uploadBatch")
    @Operation(summary = "用例文件素材批量上传")
    @OperateLog(type = OperateTypeEnum.OTHER)
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:uploadBatch')")
    public Result<String> uploadFileBatch(@RequestPart("file") MultipartFile[] files,
                                          @RequestParam("recordId") Integer recordId,
                                          @RequestParam("batchName") String batchName,
                                          @RequestParam("materialClassify")Integer materialClassify,
                                          @RequestParam("executionTime") String executionTime){
        return Result.ok(uploadFileBatchService.uploadFileBatch(files,recordId,batchName,materialClassify, DateUtils.parse(executionTime,"yyyyMMddHHmmss")));
    }

    /**
     * 查询素材详情
     * @param recordId
     * @param testCaseId
     * @return
     */
    @PostMapping("/testCase/material/info")
    @Operation(summary = "查询素材详情")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:info')")
    public Result<TestStateInfoResponse> findMaterialList(@RequestParam("recordId")Integer recordId, @RequestParam("testCaseId")Integer testCaseId){
        return Result.ok(testCaseMaterialService.findMaterialList(recordId,testCaseId));
    }

    /**
     * 修改状态
     * @param recordId
     * @param testCaseId
     */
    @PostMapping("/testCase/material/editState")
    @Operation(summary = "测试用例素材状态修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> editState(@RequestParam("recordId") Integer recordId,
                                  @RequestParam("testCaseId") Integer testCaseId,
                                  @RequestParam("state") String state){
        testCaseMaterialService.editState(recordId,testCaseId,state);
        return Result.ok();
    }

    /**
     * 获取图片 url
     * @param photoName
     * @return
     */
    @PostMapping("/testCase/material/photoUrl")
    @Operation(summary = "测试用例素材图片url")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:info') || hasAuthority('beeeval:function_tree_m:page')")
    public Result<String> queryPhoto(@RequestParam("photoName") String photoName){
        String url = testCaseMaterialService.queryPhoto(photoName);
        return Result.ok(url);
    }

    /**
     * 获取视频url
     * @param videoName
     * @return
     */
    @PostMapping("/testCase/material/videoUrl")
    @Operation(summary = "测试用例素材视频url")
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:info') || hasAuthority('beeeval:function_tree_m:page')")
    public Result<String> queryVideo(@RequestParam("videoName") String videoName){
        String url = testCaseMaterialService.queryVideo(videoName);
        return Result.ok(url);
    }


    @PostMapping("/testCase/material/show")
    @Operation(summary = "测试用例素材显示")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:edit')")
    public Result<Void> setMaterialShow(@RequestBody TestCaseMaterialShowRequest request) {
        testCaseMaterialService.setMaterialShow(request);
        return Result.ok();
    }


    /**
     * 素材删除
     */
    @PostMapping("/testCase/material/delete")
    @Operation(summary = "测试用例素材删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('test_platform:funciton_tree:delete')")
    public Result<String> deleteMaterial(@RequestBody List<Long> ids){
        testCaseMaterialService.deleteMaterial(ids);
        return Result.ok();
    }


}
