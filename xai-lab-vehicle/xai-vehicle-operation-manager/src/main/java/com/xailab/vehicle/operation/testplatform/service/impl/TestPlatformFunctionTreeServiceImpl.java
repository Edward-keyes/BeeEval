package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.framework.common.exception.ServerException;
import com.xailab.vehicle.framework.common.utils.JsonUtils;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.*;
import com.xailab.vehicle.operation.testplatform.dao.*;
import com.xailab.vehicle.operation.testplatform.entity.*;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeDataStateEnum;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeEvaluteStateEnum;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTestCaseRateStateEnum;
import com.xailab.vehicle.operation.testplatform.pojo.request.*;
import com.xailab.vehicle.operation.testplatform.pojo.response.*;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformFunctionTreeService;
import com.xailab.vehicle.operation.testplatform.vo.*;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: TestPlatformFunctionTreeServiceImpl
 * @Description:
 * @author: liulin
 * @date: 2025/4/28 22:47
 */
@Service
@Slf4j
@RequiredArgsConstructor
@DS("test_platform")
public class TestPlatformFunctionTreeServiceImpl extends BaseServiceImpl<TestPlatformVehicleTestCaseDao, TestPlatformVehicleTestCaseEntity> implements TestPlatformFunctionTreeService {
    private final TestPlatformVehicleTestCaseDao testPlatformVehicleTestCaseDao;
    private final TestQuesDao testQuesDao;
    private final TestQuesOptionsDao testQuesOptionsDao;
    private final TestPlatformVehiclePlanDetailDao testPlatformVehiclePlanDetailDao;
    private final TestProcessDao testProcessDao;
    private final TestPlatformVehicleTestScenarioDao testPlatformVehicleTestScenarioDao;
    private final TestProcessOptionsDao testProcessOptionsDao;
    private final TestPlatformVehicleTestRecordDao testPlatformVehicleTestRecordDao;
    private final TestPlatformVehicleTestStateDao testPlatformVehicleTestStateDao;
    private final QuesStateDao quesStateDao;
    private final QuesStateOptionsDao quesStateOptionsDao;
    private final ProcessStateDao processStateDao;
    private final ProcessStateOptionsDao processStateOptionsDao;
    private final ErrorTypeDao errorTypeDao;
    private final FunctionTreeStateDao functionTreeStateDao;
    private final FunctionTreeFirstTypeDao functionTreeFirstTypeDao;

    @Value("${test_platform.function-tree.defaultScenarioId:6}")
    private Integer defaultScenarioId;
    @Value("${test_platform.function-tree.defaultScenarioTask:功能走查}")
    private String defaultScenarioTask;

    /**
     * 功能树分页查询
     * @param request
     * @return
     */
    @Override
    public PageResult<TestPlatformFunctionTreeResponse> page(TestPlatformFunctionTreeRequest request) {
        log.info("功能树分页查询请求参数：{}", JsonUtils.toJsonString(request));
        if (StringUtils.isBlank(request.getFunctionEvaluate())){
            request.setFunctionEvaluate(null);
        }
        if (StringUtils.isBlank(request.getType())){
            request.setType(null);
        }
        IPage<TestPlatformFunctionTreeVo> testPlatformFunctionTreeVoIPage = testPlatformVehicleTestCaseDao.
                selectFunctionTreeTag(getPage(request), request.getType(), defaultScenarioId,
                        request.getFunctionId(),
                        request.getFunctionEvaluate(),
                        request.getDataState(),
                        request.getTestRecordId(),
                        request.getTestCaseId()
                );
        List<TestPlatformFunctionTreeVo> records = testPlatformFunctionTreeVoIPage.getRecords();
        List<TestPlatformFunctionTreeResponse> list = new ArrayList<>();
        for (TestPlatformFunctionTreeVo it : records) {
            TestPlatformFunctionTreeResponse convert = TestPlatformVehicleTestCaseConvert.INSTANCE.convert(it);
            if (defaultScenarioTask.equals(convert.getScenarioTask())){
                convert.setTagType(1);
            }else{
                convert.setTagType(0);
            }
            //根据场景任务获取测试用例
            List<FunctionTreeCaseVo> entities = testPlatformVehicleTestCaseDao.selectTreeCaseList(defaultScenarioId,
                    it.getTaskDetail(),null,null,request.getTestRecordId());
            List<TestPlatformFunctionTreeCaseResponse> treeCaseResponses = TestPlatformVehicleTestCaseConvert.INSTANCE.convertListRes(entities);
            treeCaseResponses.forEach(item->{
                if (defaultScenarioTask.equals(item.getScenarioTask())){
                    item.setCaseType(1);
                }else{
                    item.setCaseType(0);
                }
                item.setFunctionDomainName(it.getFunctionDomainName());
                item.setFunctionId(it.getFunctionId());
            });
            convert.setTestCaseInfo(treeCaseResponses);
            String functionTag = convert.getFunctionTag();
            String[] split = functionTag.split("-");
            if (!Arrays.isNullOrEmpty(split)) {
                convert.setFunctionTagId(split[0]);
                convert.setFunctionTagName(split[split.length - 1]);
                convert.setTag(split[0]);
            }else {
                convert.setFunctionTagId(functionTag);
                convert.setFunctionTagName(functionTag);
                convert.setTag(split[0]);
            }
//            //设置功能评价状态和数据状态
//            FunctionTreeStateEntity functionTreeStateEntity = null;
//            if (Objects.nonNull( request.getTestRecordId())){
//                functionTreeStateEntity = functionTreeStateDao.selectOne(Wrappers.<FunctionTreeStateEntity>lambdaQuery()
//                                .eq(FunctionTreeStateEntity::getTaskDetail, it.getTaskDetail())
////                        .eq(FunctionTreeStateEntity::getScenarioTask, it.getScenarioTask())
////                        .eq(FunctionTreeStateEntity::getFunctionDomainId, it.getFunctionId())
//                                .eq(FunctionTreeStateEntity::getRecordId, request.getTestRecordId())
//                                .eq(FunctionTreeStateEntity::getDeleted, 0)
//                );
//            }
//            if (Objects.nonNull(functionTreeStateEntity)){
//                convert.setFunctionEvaluate(functionTreeStateEntity.getFunctionEvaluate());
//                convert.setDataState(functionTreeStateEntity.getDataState());
//            }else {
//                convert.setFunctionEvaluate(FunctionTreeEvaluteStateEnum.NA.getValue());
//                convert.setDataState(FunctionTreeDataStateEnum.LACK_OF_DATA.getValue());
//            }
            if (Objects.isNull(it.getFunctionEvaluate())|| Objects.isNull(it.getDataState())){
                convert.setFunctionEvaluate(FunctionTreeEvaluteStateEnum.UN.getValue());
                convert.setDataState(FunctionTreeDataStateEnum.LACK_OF_DATA.getValue());
            }
            list.add(convert);
        }
        return new PageResult<>(list, testPlatformFunctionTreeVoIPage.getTotal());
    }

    /**
     * 功能树新增
     */
    @Override
    public void add(FunctionTreeAddRequest request) {
        FunctionTreeFirstTypeEntity typeEntity = functionTreeFirstTypeDao.selectOne(Wrappers.<FunctionTreeFirstTypeEntity>lambdaQuery()
                .eq(FunctionTreeFirstTypeEntity::getDeleted, 0)
                .eq(FunctionTreeFirstTypeEntity::getLabel,request.getFirstlyType())
        );
        if (Objects.isNull(typeEntity)){
            throw new ServerException("一级标签错误或已删除");
        }
        Integer secondaryLevel = 1;
        Integer threeLevel = 1;
        //判断当前二级指标是否是新增
        if (!request.getScenarioTask().startsWith(request.getFirstlyType())) {
            //新增 获取二级指标最大
            String maxScenarioTask = testPlatformVehicleTestCaseDao.findMaxScenarioTask(request.getFirstlyType(), defaultScenarioId);
            if (!StringUtils.isBlank(maxScenarioTask)) {
                String[] split = maxScenarioTask.split("-");
                String tag = split[0];
                String levelStr = tag.substring(1);
                int level = Integer.parseInt(levelStr);
                secondaryLevel = ++level;
                if (secondaryLevel >= 10) {
                    throw new ServerException("二级标签数量必须在10以下");
                }
            }
            request.setScenarioTask(request.getFirstlyType() + secondaryLevel + "-" + request.getScenarioTask());
        } else {
            String[] split = request.getScenarioTask().split("-");
            String tag = split[0];
            String levelStr = tag.substring(1);
            secondaryLevel = Integer.parseInt(levelStr);
        }
        //判断当前三级指标是否是新增
        if (!request.getTaskDetail().startsWith(request.getFirstlyType())) {
            //新增 获取三级指标最大
            String secondaryTag = request.getFirstlyType() + secondaryLevel;
            String maxScenarioTask = testPlatformVehicleTestCaseDao.findMaxTaskDetail(secondaryTag, defaultScenarioId);
            if (!StringUtils.isBlank(maxScenarioTask)) {
                String[] split = maxScenarioTask.split("-");
                String tag = split[0];
                String levelStr = tag.substring(2);
                int level = Integer.parseInt(levelStr);
                threeLevel = ++level;
                if (threeLevel >= 100) {
                    throw new ServerException("三级标签数量必须在100以下");
                }
            }
            request.setTaskDetail(secondaryTag + threeLevel + "-" + request.getTaskDetail());
        }
        log.info("功能树新增 指标编号，二：{}，三：{}",secondaryLevel,threeLevel);
        FunctionTreeTestCaseAddRequest addRequest = new FunctionTreeTestCaseAddRequest();
        addRequest.setFunctionId(request.getFunctionId());
        addRequest.setFunctionTag(request.getTaskDetail());
        addRequest.setTestcaseContent(request.getTestcaseContent());
        addRequest.setScenarioTask(request.getTagType().equals(0)?request.getScenarioTask():defaultScenarioTask);
        addRequest.setTestRecordId(request.getTestRecordId());
        testCaseAdd(addRequest);
    }

    /**
     * 功能状态编辑
     * @param request
     */
    @Override
    public void edit(FunctionTreeStateEditRequest request) {
        //判断参数是否正确
        FunctionTreeEvaluteStateEnum evaluteStateEnum = FunctionTreeEvaluteStateEnum.paseEnum(request.getFunctionEvaluate());
        if (Objects.isNull(evaluteStateEnum)){
            throw new ServerException("功能评价状态错误");
        }
        FunctionTreeDataStateEnum stateEnum = FunctionTreeDataStateEnum.paseEnum(request.getDataState());
        if (Objects.isNull(stateEnum)){
            throw new ServerException("数据状态错误");
        }

        //查询功能评价状态和数据状态
        FunctionTreeStateEntity functionTreeStateEntity = functionTreeStateDao.selectOne(Wrappers.<FunctionTreeStateEntity>lambdaQuery()
                .eq(FunctionTreeStateEntity::getTaskDetail, request.getTaskDetail())
//                .eq(FunctionTreeStateEntity::getScenarioTask, request.getScenarioTask())
//                .eq(FunctionTreeStateEntity::getFunctionDomainId, request.getFunctionDomainId())
                .eq(FunctionTreeStateEntity::getRecordId, request.getRecordId())
        );
        if (Objects.isNull(functionTreeStateEntity)){
            FunctionTreeStateEntity convert = FunctionTreeStateConvert.INSTANCE.convert(request);
            functionTreeStateDao.insert(convert);
            return;
        }
        functionTreeStateEntity.setDataState(request.getDataState());
        functionTreeStateEntity.setFunctionEvaluate(request.getFunctionEvaluate());
        functionTreeStateDao.insertOrUpdate(functionTreeStateEntity);
    }

    /**
     * 查询详情
     * @param request
     * @return
     */
    @Override
    public List<FunctionTreeInfoResponse> queryInfo(FunctionTreeInfoRequest request) {
        return functionTreeStateDao.queryInfo( request.getTaskDetail());
    }

    /**
     * 获取功能一级标签
     * @return
     */
    @Override
    public List<FunctionTreeLevelTypeResponse> queryTypeFirst() {
        List<FunctionTreeFirstTypeEntity> functionTreeFirstTypeEntities = functionTreeFirstTypeDao.selectList(Wrappers.<FunctionTreeFirstTypeEntity>lambdaQuery()
                .eq(FunctionTreeFirstTypeEntity::getDeleted, 0)
        );
        return FunctionTreeFirstTypeConvert.INSTANCE.convertListRes(functionTreeFirstTypeEntities);
    }

    /**
     * 获取功能二级级标签
     * @return
     */
    @Override
    public List<FunctionTreeLevelTypeResponse> queryTypeSecond(String label) {
        FunctionTreeFirstTypeEntity typeEntity = functionTreeFirstTypeDao.selectOne(Wrappers.<FunctionTreeFirstTypeEntity>lambdaQuery()
                .eq(FunctionTreeFirstTypeEntity::getDeleted, 0)
                .eq(FunctionTreeFirstTypeEntity::getLabel,label)
        );
        if (Objects.isNull(typeEntity)){
            throw new ServerException("一级标签错误或已删除");
        }
        QueryWrapper<TestPlatformVehicleTestCaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT scenario_task");
        List<TestPlatformVehicleTestCaseEntity> entities = testPlatformVehicleTestCaseDao.selectList(queryWrapper.lambda()
                .eq(TestPlatformVehicleTestCaseEntity::getScenarioId,defaultScenarioId)
                .like(TestPlatformVehicleTestCaseEntity::getScenarioTask, label + "%")
        );
        List<FunctionTreeLevelTypeResponse> list = entities.stream().map(it -> {
            FunctionTreeLevelTypeResponse response = new FunctionTreeLevelTypeResponse();
            response.setLabel(it.getScenarioTask());
            response.setLabelName(it.getScenarioTask());
            return response;
        }).collect(Collectors.toList());
//        list.add(new FunctionTreeLevelTypeResponse(defaultScenarioTask,defaultScenarioTask));
        return list;
    }

    /**
     * 功能树状态更新
     * @param recordId
     * @param taskDetail
     * @return
     */
    @Override
    public Result<Void> updateFunctionTreeState(Integer recordId, String taskDetail,Integer dataState) {
        boolean isUpdate = true;
        FunctionTreeStateEntity stateEntity = functionTreeStateDao.selectOne(Wrappers.<FunctionTreeStateEntity>lambdaQuery()
                .eq(FunctionTreeStateEntity::getRecordId, recordId)
                .eq(FunctionTreeStateEntity::getTaskDetail, taskDetail)
        );
        if (Objects.isNull(stateEntity)) {
            stateEntity = new FunctionTreeStateEntity();
            stateEntity.setRecordId(recordId);
            stateEntity.setTaskDetail(taskDetail);
            stateEntity.setDataState(FunctionTreeDataStateEnum.LACK_OF_DATA.getValue());
            stateEntity.setFunctionEvaluate(FunctionTreeEvaluteStateEnum.UN.getValue());
        }
        if (FunctionTreeDataStateEnum.LACK_OF_DATA.equals(stateEntity.getDataState()) && FunctionTreeDataStateEnum.SYNCHRONIZATION.equals(dataState)){
            //查询所有的测试用例
            List<Integer> caseIds = testPlatformVehicleTestCaseDao.selectIdsByTaskDetail(taskDetail);
            log.info("功能树状态更新 测试用例数量：{}",caseIds.size());
            //查询功能树状
            List<TestPlatformVehicleTestStateEntity> testStateEntities = testPlatformVehicleTestStateDao.selectList(Wrappers.<TestPlatformVehicleTestStateEntity>lambdaQuery()
                    .eq(TestPlatformVehicleTestStateEntity::getRecordId, recordId)
                    .in(TestPlatformVehicleTestStateEntity::getTestcaseId, caseIds)
            );
            Map<Integer, TestPlatformVehicleTestStateEntity> collect = testStateEntities.stream().collect(Collectors.toMap(TestPlatformVehicleTestStateEntity::getTestcaseId, it -> it));

            for (Integer caseId : caseIds) {
                TestPlatformVehicleTestStateEntity testStateEntity = collect.get(caseId);
                if (Objects.isNull(testStateEntity) ||
                        FunctionTreeTestCaseRateStateEnum.NA.equals(testStateEntity.getTestCaseRate())){
                    log.info("功能树状态不进行更新，recordId:{},tagId:{}",recordId,taskDetail);
                    isUpdate = false;
                    break;
                }
            }
        }
        if (!isUpdate || FunctionTreeDataStateEnum.ONLINE.equals(stateEntity.getDataState())){
            log.info("功能树状态不进行更新：isUPdate：{}，current:{}",isUpdate,stateEntity.getDataState());
            return Result.ok();
        }
        if (!stateEntity.getDataState().equals(dataState)){
            log.info("功能树状态更新：{},recordId:{},tagId:{}",dataState,recordId,taskDetail);
            stateEntity.setDataState(dataState);
            functionTreeStateDao.insertOrUpdate(stateEntity);
        }
        return Result.ok();
    }

    @Override
    public Result<Void> updateFunctionTreeStateAsync(Integer recordId, String taskDetail, Integer dataState) {
        boolean isUpdate = true;
        FunctionTreeStateEntity stateEntity = functionTreeStateDao.selectOne(Wrappers.<FunctionTreeStateEntity>lambdaQuery()
                .eq(FunctionTreeStateEntity::getRecordId, recordId)
                .eq(FunctionTreeStateEntity::getTaskDetail, taskDetail)
        );
        if (Objects.isNull(stateEntity)) {
            stateEntity = new FunctionTreeStateEntity();
            stateEntity.setRecordId(recordId);
            stateEntity.setTaskDetail(taskDetail);
            stateEntity.setDataState(FunctionTreeDataStateEnum.LACK_OF_DATA.getValue());
            stateEntity.setFunctionEvaluate(FunctionTreeEvaluteStateEnum.UN.getValue());
        }
        if (!FunctionTreeDataStateEnum.LACK_OF_DATA.equals(stateEntity.getDataState())){
            log.info("功能树状态不进行更新：current:{}",stateEntity.getDataState());
            return Result.ok();
        }
        if (stateEntity.getDataState().equals(dataState)){
            log.info("功能树状态不更新，状态一致：{},recordId:{},tagId:{}",dataState,recordId,taskDetail);
            return Result.ok();
        }

        if (FunctionTreeDataStateEnum.SYNCHRONIZATION.equals(dataState)){
            //查询所有的测试用例
            List<Integer> caseIds = testPlatformVehicleTestCaseDao.selectIdsByTaskDetail(taskDetail);
            log.info("功能树状态更新 测试用例数量：{}",caseIds.size());
            //查询功能树状
            List<TestPlatformVehicleTestStateEntity> testStateEntities = testPlatformVehicleTestStateDao.selectList(Wrappers.<TestPlatformVehicleTestStateEntity>lambdaQuery()
                    .eq(TestPlatformVehicleTestStateEntity::getRecordId, recordId)
                    .in(TestPlatformVehicleTestStateEntity::getTestcaseId, caseIds)
            );
            Map<Integer, TestPlatformVehicleTestStateEntity> collect = testStateEntities.stream().collect(Collectors.toMap(TestPlatformVehicleTestStateEntity::getTestcaseId, it -> it));

            for (Integer caseId : caseIds) {
                TestPlatformVehicleTestStateEntity testStateEntity = collect.get(caseId);
                if (Objects.isNull(testStateEntity) ||
                        FunctionTreeTestCaseRateStateEnum.NA.equals(testStateEntity.getTestCaseRate())){
                    log.info("功能树状态不进行更新，recordId:{},tagId:{}",recordId,taskDetail);
                    isUpdate = false;
                    break;
                }
            }
        }
        if (!isUpdate){
            log.info("功能树状态不进行更新：isUPdate：{}，current:{}",isUpdate,stateEntity.getDataState());
            return Result.ok();
        }
        log.info("功能树状态更新：{},recordId:{},tagId:{}",dataState,recordId,taskDetail);
        stateEntity.setDataState(dataState);
        functionTreeStateDao.insertOrUpdate(stateEntity);
        return Result.ok();
    }

    /**
     * 功能树状态同步
     **/
    @Override
    public Result<Void> functionTreeStateSync() {
        // 查询出所有的功能树测试用例
        List<TestPlatformFunctionTreeVo> testPlatformFunctionTreeVos = testPlatformVehicleTestCaseDao.selectFunctionTreeTagList(defaultScenarioId);
        if (CollectionUtils.isEmpty(testPlatformFunctionTreeVos)){
            log.info("查询出的功能id为空");
            return Result.error("查询数据为空");
        }
        //查询出所有的测试任务
        List<TestPlatformVehicleTestRecordEntity> recordEntities = testPlatformVehicleTestRecordDao.selectList(Wrappers.<TestPlatformVehicleTestRecordEntity>lambdaQuery());
        if (CollectionUtils.isEmpty(recordEntities)){
            log.info("查询出的测试任务为空");
            return Result.error("查询数据为空");
        }
        //遍历
        for (TestPlatformVehicleTestRecordEntity recordEntity : recordEntities) {
            for (TestPlatformFunctionTreeVo testPlatformFunctionTreeVo : testPlatformFunctionTreeVos) {
                log.info("正在同步：recordId:{},TagId:{}",recordEntity.getId(),testPlatformFunctionTreeVo.getTaskDetail());
                updateFunctionTreeState(recordEntity.getId(),testPlatformFunctionTreeVo.getTaskDetail(), FunctionTreeDataStateEnum.SYNCHRONIZATION.getValue());
            }
        }
        return Result.ok();
    }


    /**
     * 功能树测试用例 操作
     */

    /**
     * 测试用例详情查询
     * @param testCaseId
     * @return
     */
    @Override
    public FunctionTreeCaseProcessResponse findTestCaseProcess(Integer testCaseId) {
        //查询测试用例
        TestPlatformVehicleTestCaseEntity testCaseEntity = testPlatformVehicleTestCaseDao.selectById(testCaseId);
        if (Objects.isNull(testCaseEntity)){
            throw new ServerException("测试用例不存在或已删除");
        }
//        TestPlatformVehiclePlanDetailEntity planDetail = testPlatformVehiclePlanDetailDao.selectById(testCaseEntity.getFunctionId());
//        if (Objects.isNull(planDetail)){
//            throw new ServerException("测试用例对应域不存在");
//        }
        FunctionTreeCaseProcessResponse response = new FunctionTreeCaseProcessResponse();
        response.setId(testCaseId);
        if (!defaultScenarioTask.equals(testCaseEntity.getScenarioTask())){
            response.setCaseType(0);
            response.setReferenceResult(testCaseEntity.getReferenceResult());
            response.setInspectionPoint(testCaseEntity.getInspectionPoint());
            return response;
        }
        response.setCaseType(1);
        TestProcessEntity testProcessEntity = testProcessDao.selectOne(Wrappers.<TestProcessEntity>lambdaQuery()
                .eq(TestProcessEntity::getLevelThreeName, testCaseEntity.getTaskDetail())
                .eq(TestProcessEntity::getProcessTitle, testCaseEntity.getTestcaseContent())
//                .eq(TestProcessEntity::getLevelOneName,planDetail.getPlanDetailName())
        );
        if (Objects.isNull(testProcessEntity)){
            return response;
        }
        FunctionTreeCaseProcessInfo functionTreeCaseProcessInfo = TestProcessConvert.INSTANCE.convertInfo(testProcessEntity);
        if ("1".equals(functionTreeCaseProcessInfo.getOptionsType())){
            List<TestProcessOptionsEntity> optionsEntities = testProcessOptionsDao.selectList(Wrappers.<TestProcessOptionsEntity>lambdaQuery()
                    .eq(TestProcessOptionsEntity::getProcessId, testProcessEntity.getId())
            );
            List<TestProcessOptionsVO> testProcessOptionsVOS = TestProcessOptionsConvert.INSTANCE.convertList(optionsEntities);
            functionTreeCaseProcessInfo.setOptions(testProcessOptionsVOS);
        }
        response.setProcessInfo(functionTreeCaseProcessInfo);
        return response;
    }


    /**
     * 功能树测试用例选项 添加和编辑
     * @param request
     */
    @Override
    public void testCaseProcessEdit(FunctionTreeCaseProcessRequest request) {
        //查询用例对应用例id
        //查询测试用例
        TestPlatformVehicleTestCaseEntity testCaseEntity = testPlatformVehicleTestCaseDao.selectById(request.getId());
        if (Objects.isNull(testCaseEntity)){
            throw new ServerException("测试用例不存在或已删除");
        }
        if (request.getCaseType().equals(0)){
            log.info("当前编辑为测试用例类型为 评价");
            testCaseEntity.setReferenceResult(request.getReferenceResult());
            testCaseEntity.setInspectionPoint(request.getInspectionPoint());
            testPlatformVehicleTestCaseDao.insertOrUpdate(testCaseEntity);
            return;
        }
        FunctionTreeCaseProcessInfo processInfo = request.getProcessInfo();
        if (Objects.isNull(processInfo)){
            throw new ServerException("选项详情不能为空");
        }
        TestPlatformVehiclePlanDetailEntity planDetail = testPlatformVehiclePlanDetailDao.selectById(testCaseEntity.getFunctionId());
        if (Objects.isNull(planDetail)){
            throw new ServerException("查询域失败");
        }
        //判断是否有id
        TestProcessEntity processEntity = new TestProcessEntity();
        if (Objects.nonNull(processInfo.getId())){
            TestProcessEntity testProcessEntity = testProcessDao.selectById(processInfo.getId());
            if (Objects.isNull(testProcessEntity)){
                throw new ServerException("功能走查数据查询失败");
            }
            processEntity =testProcessEntity;
        }else {
            //判断 是否重复
            TestProcessEntity testProcessEntity = testProcessDao.selectOne(Wrappers.<TestProcessEntity>lambdaQuery()
                            .eq(TestProcessEntity::getLevelThreeName, testCaseEntity.getTaskDetail())
                            .eq(TestProcessEntity::getProcessTitle, testCaseEntity.getTestcaseContent())
//                .eq(TestProcessEntity::getLevelOneName,planDetail.getPlanDetailName())
            );
            if (Objects.nonNull(testProcessEntity)){
                throw new ServerException("该功能走查用例已存在");
            }
        }
        TestProcessConvert.INSTANCE.convertEntity(processInfo,processEntity);
        processEntity.setLevelOneName(planDetail.getPlanDetailName());
        processEntity.setLevelTwoName(defaultScenarioTask);
        processEntity.setLevelThreeName(testCaseEntity.getTaskDetail());
        processEntity.setProcessTitle(testCaseEntity.getTestcaseContent());
        testProcessDao.insertOrUpdate(processEntity);
        if ("1".equals(processInfo.getOptionsType()) && CollectionUtils.isNotEmpty(processInfo.getOptions())){
            //保存选项
            List<TestProcessOptionsVO> options = processInfo.getOptions();
            TestProcessEntity finalProcessEntity = processEntity;
            List<TestProcessOptionsEntity> list = options.stream().map(it->{
                TestProcessOptionsEntity convert = TestProcessOptionsConvert.INSTANCE.convert(it);
                convert.setProcessId(finalProcessEntity.getId());
                return convert;
            }).toList();
            testProcessOptionsDao.insertOrUpdate(list);
        }

    }

    /**
     * 用例新增
     * @param request
     */
    @Override
    public void testCaseAdd(FunctionTreeTestCaseAddRequest request) {
        //查询域id是否为空
        TestPlatformVehiclePlanDetailEntity planDetail = testPlatformVehiclePlanDetailDao.selectById(request.getFunctionId());
        if (Objects.isNull(planDetail)){
            throw new ServerException("功能域不存在");
        }
        //判断是否重复
        Long caseEntity = testPlatformVehicleTestCaseDao.selectCount(Wrappers.<TestPlatformVehicleTestCaseEntity>lambdaQuery()
                .eq(TestPlatformVehicleTestCaseEntity::getFunctionId, request.getFunctionId())
                .eq(TestPlatformVehicleTestCaseEntity::getScenarioId, defaultScenarioId)
                .eq(TestPlatformVehicleTestCaseEntity::getTestcaseContent, request.getTestcaseContent())
                .eq(TestPlatformVehicleTestCaseEntity::getScenarioTask, request.getScenarioTask())
                .eq(TestPlatformVehicleTestCaseEntity::getTaskDetail, request.getFunctionTag())
        );
        if (Objects.nonNull(caseEntity) && caseEntity>0){
            throw new ServerException("该测试用例已经存在");
        }
        //查询场景详情
        TestPlatformVehicleTestScenarioEntity scenarioEntity = testPlatformVehicleTestScenarioDao.selectById(defaultScenarioId);
        TestPlatformVehicleTestCaseEntity testCaseEntity = TestPlatformVehicleTestCaseConvert.INSTANCE.convert(request, scenarioEntity);
        testPlatformVehicleTestCaseDao.insert(testCaseEntity);

        if (Objects.nonNull(request.getTestRecordId())){
            TestPlatformVehicleTestStateEntity stateEntity = new TestPlatformVehicleTestStateEntity();
            stateEntity.setTestStatus(0);
            stateEntity.setRecordId(request.getTestRecordId());
            stateEntity.setTestcaseId(testCaseEntity.getId());
            stateEntity.setIsSuccessful(-1);
            testPlatformVehicleTestStateDao.insert(stateEntity);
        }

    }

    /**
     * 查询测试用例评价详情
     * @param testCaseId 测试用例id
     * @return
     */
    @Override
    public FunctionTreeCaseEvaluateResponse findCaseEvaluateInfo(Integer testCaseId) {
        //查询测试用例
        TestPlatformVehicleTestCaseEntity testCaseEntity = testPlatformVehicleTestCaseDao.selectById(testCaseId);
        if (Objects.isNull(testCaseEntity)){
            throw new ServerException("测试用例不存在或已删除");
        }
        //判断当前是否是测试用例评价
        if (defaultScenarioTask.equals(testCaseEntity.getScenarioTask())){
            throw new ServerException("当前用例类型不为功能评价");
        }
        FunctionTreeCaseEvaluateResponse response = new FunctionTreeCaseEvaluateResponse();
        response.setId(testCaseId);
        response.setReferenceResult(testCaseEntity.getReferenceResult());
        response.setInspectionPoint(testCaseEntity.getInspectionPoint());
        response.setIsEnable(testCaseEntity.getIsEnable());
        response.setTestcaseContent(testCaseEntity.getTestcaseContent());
        return response;
    }

    /**
     * 用例评价详情新增
     * @param request
     */
    @Override
    public void updateCaseEvaluate(FunctionTreeCaseEvaluateRequest request) {
        //查询测试用例
        TestPlatformVehicleTestCaseEntity testCaseEntity = testPlatformVehicleTestCaseDao.selectById(request.getId());
        if (Objects.isNull(testCaseEntity)){
            throw new ServerException("测试用例不存在或已删除");
        }
        //判断当前是否是测试用例评价
        if (defaultScenarioTask.equals(testCaseEntity.getScenarioTask())){
            throw new ServerException("当前用例类型不为功能评价");
        }
        testPlatformVehicleTestCaseDao.update(Wrappers.<TestPlatformVehicleTestCaseEntity>lambdaUpdate()
                .set(TestPlatformVehicleTestCaseEntity::getReferenceResult,request.getReferenceResult())
                .set(TestPlatformVehicleTestCaseEntity::getInspectionPoint,request.getInspectionPoint())
                .set(TestPlatformVehicleTestCaseEntity::getIsEnable,request.getIsEnable())
                .set(TestPlatformVehicleTestCaseEntity::getTestcaseContent,request.getTestcaseContent())
                .eq(TestPlatformVehicleTestCaseEntity::getId,request.getId())
        );
    }


    /**
     * 查询测试用例功能走查详情
     * @param request
     * @return
     */
    @Override
    public List<FunctionTreeCaseWalkthroughResponse> findCaseWalkthroughInfo(FunctionTreeCaseWalkthroughRequest request) {
        //根据三级
        List<TestProcessEntity> testProcessEntities = testProcessDao.selectList(Wrappers.<TestProcessEntity>lambdaQuery()
                .eq(TestProcessEntity::getLevelThreeName, request.getLevelThreeName())
        );
        if (CollectionUtils.isEmpty(testProcessEntities)){
            return new ArrayList<>();
        }
        List<FunctionTreeCaseWalkthroughResponse> list = testProcessEntities.stream().map(it -> {
            FunctionTreeCaseWalkthroughResponse response = new FunctionTreeCaseWalkthroughResponse();
            response.setId(it.getId());
            response.setLevelThreeName(it.getLevelThreeName());
            response.setStep(it.getStep());
            response.setDescription(it.getDescription());
            response.setTip(it.getTip());
            response.setProcessTitle(it.getProcessTitle());

            //设置预设问题
            FunctionTreeCaseWalkthroughResponse.WalkthroughAlternativeQuestions questions = new FunctionTreeCaseWalkthroughResponse.WalkthroughAlternativeQuestions();
            questions.setQuesOne(it.getQuesOne());
            questions.setQuesTwo(it.getQuesTwo());
            questions.setQuesThree(it.getQuesThree());
            response.setAlternativeQuestions(questions);

            //选项设置
            FunctionTreeCaseWalkthroughResponse.WalkthroughOptionSettings settings = new FunctionTreeCaseWalkthroughResponse.WalkthroughOptionSettings();
            settings.setOptionsType(it.getOptionsType());
            settings.setOptionsTitle(it.getOptionsTitle());
            List<TestProcessOptionsEntity> optionsEntities = testProcessOptionsDao.selectList(Wrappers.<TestProcessOptionsEntity>lambdaQuery()
                    .eq(TestProcessOptionsEntity::getProcessId, it.getId())
            );
            List<TestProcessOptionsVO> testProcessOptionsVOS = TestProcessOptionsConvert.INSTANCE.convertList(optionsEntities);
            settings.setOptions(testProcessOptionsVOS);
            response.setOptionSettings(settings);
            return response;
        }).toList();
        return list;
    }

    /**
     * 编辑用例功能走查详情
     * @param request
     */
    @Transactional
    @Override
    public void updateCaseWalkthrough(List<FunctionTreeCaseWalkthroughResponse> request) {
        if (CollectionUtils.isEmpty(request)) {
            throw new ServerException("功能走查详情编辑不能为空");
        }
        for (FunctionTreeCaseWalkthroughResponse it : request) {
            TestProcessEntity testProcessEntity = TestProcessConvert.INSTANCE.convertEntity(it, defaultScenarioTask);
            //判断当前用例是否重复
            List<TestProcessEntity> testProcessEntities = testProcessDao.selectList(Wrappers.<TestProcessEntity>lambdaQuery()
                    .eq(TestProcessEntity::getLevelThreeName, testProcessEntity.getLevelThreeName())
                    .eq(TestProcessEntity::getProcessTitle, testProcessEntity.getProcessTitle())
                    .ne(Objects.nonNull(testProcessEntity.getId()),TestProcessEntity::getId,testProcessEntity.getId())
            );
            if (CollectionUtils.isNotEmpty(testProcessEntities)){
                throw new ServerException("当前步骤:"+it.getStep()+" 流程题目已存在");
            }
            //TODOid为空创建对应用例
            if (Objects.isNull(testProcessEntity.getId())){
                TestPlatformVehicleTestCaseEntity testCaseEntity = new TestPlatformVehicleTestCaseEntity();
                testCaseEntity.setFunctionId(it.getFunctionId());
                testCaseEntity.setIsEnable(Boolean.TRUE);
                testCaseEntity.setTestcaseContent(it.getProcessTitle());
                testCaseEntity.setScenarioId(defaultScenarioId);
                testCaseEntity.setScenarioTask(defaultScenarioTask);
                testCaseEntity.setTaskDetail(it.getLevelThreeName());
                testCaseEntity.setPrimaryMetric("功能树检查");
                testCaseEntity.setSecondaryMetric("功能树检查");
                testCaseEntity.setTertiaryMetric("功能树检查");
                testPlatformVehicleTestCaseDao.insert(testCaseEntity);
            }
            testProcessDao.insertOrUpdate(testProcessEntity);
            //获取当前所有的选项
            if (Objects.nonNull(testProcessEntity.getId()) && Objects.nonNull(it.getOptionSettings())) {
                List<TestProcessOptionsEntity> optionsEntities = testProcessOptionsDao.selectList(Wrappers.<TestProcessOptionsEntity>lambdaQuery()
                        .eq(TestProcessOptionsEntity::getProcessId, it.getId())
                );
                List<Integer> entityIds = optionsEntities.stream().map(TestProcessOptionsEntity::getId).toList();
                List<TestProcessOptionsVO> options = it.getOptionSettings().getOptions();
                if (CollectionUtils.isEmpty(options) && !CollectionUtils.isEmpty(optionsEntities)) {
                    processOptionDelete(it, entityIds);
                }
                //获取删除的选项
                if (!CollectionUtils.isEmpty(options) && !CollectionUtils.isEmpty(optionsEntities)) {
                    List<Integer> optionId = options.stream().filter(item -> Objects.nonNull(it.getId())).map(TestProcessOptionsVO::getId).toList();
                    if (!CollectionUtils.isEmpty(optionId)) {
                        List<Integer> deleteOptions = optionsEntities.stream().map(TestProcessOptionsEntity::getId).filter(id -> !optionId.contains(id)).toList();
                        processOptionDelete(it, deleteOptions);
                    }
                }
                List<TestProcessOptionsEntity> saveUpdate = TestProcessOptionsConvert.INSTANCE.convertListEntity(options);
                if (!CollectionUtils.isEmpty(saveUpdate)) {
                    saveUpdate.forEach(item->item.setProcessId(testProcessEntity.getId()));
                    testProcessOptionsDao.insertOrUpdate(saveUpdate);
                }
            }
        }
    }

    private void processOptionDelete(FunctionTreeCaseWalkthroughResponse it, List<Integer> entityIds) {
        if (CollectionUtils.isEmpty(entityIds)){
            log.info("数据为空不进行删除");
            return;
        }
        //删除现有的所有选项
        int delete = testProcessOptionsDao.deleteByIds(entityIds);
        //删除测试数据所选的数据
        int stateDelete = processStateOptionsDao.delete(Wrappers.<ProcessStateOptionsEntity>lambdaQuery()
                .in(ProcessStateOptionsEntity::getSelectId, entityIds)
        );
        log.info("testProcessId：{},删除选项数量：{},删除测试数据：{}", it.getId(),delete,stateDelete);
    }


    /**
     * 测试数据
     */
    @Override
    public FunctionTreeCaseStateResponse testCaseStateQuery(FunctionTreeCaseStateRequest request) {
        //查询测试用例
        TestPlatformVehicleTestCaseEntity testCaseEntity = testPlatformVehicleTestCaseDao.selectById(request.getTestCaseId());
        if (Objects.isNull(testCaseEntity)){
            throw new ServerException("测试用例不存在或已删除");
        }
        //查询测试任务是否存在
        TestPlatformVehicleTestRecordEntity recordEntity = testPlatformVehicleTestRecordDao.selectById(request.getTestRecordId());
        if (Objects.isNull(recordEntity)){
            throw new ServerException("测试任务不存在或已删除");
        }
        //查询state id
        TestPlatformVehicleTestStateEntity stateEntity = testPlatformVehicleTestStateDao.selectOne(Wrappers.<TestPlatformVehicleTestStateEntity>lambdaQuery()
                .eq(TestPlatformVehicleTestStateEntity::getRecordId, recordEntity.getId())
                .eq(TestPlatformVehicleTestStateEntity::getTestcaseId, testCaseEntity.getId())
        );
        log.info("查询出的state详情为：{}",stateEntity);
        if (Objects.isNull(stateEntity)){
            //添加 test_state 数据
            stateEntity = new TestPlatformVehicleTestStateEntity();
            stateEntity.setTestStatus(0);
            stateEntity.setRecordId(recordEntity.getId());
            stateEntity.setTestcaseId(testCaseEntity.getId());
            stateEntity.setIsSuccessful(-1);
            testPlatformVehicleTestStateDao.insert(stateEntity);
        }
        FunctionTreeCaseStateResponse stateResponse =
                TestPlatformVehicleTestStateConvert.INSTANCE.convertRes(stateEntity,
                        defaultScenarioTask.equals(testCaseEntity.getScenarioTask())?1:0);
        if (Objects.nonNull(stateResponse.getErrorType()) && stateResponse.getIsSuccessful()==1){
            stateResponse.setIsSuccessful(0);
        }
        //判断是否是功能评价
        if (stateResponse.getTestCaseType().equals(0)){
            //查询功能评价题目和id
            List<FunctionTreeCaseStateEvaluateInfo> evaluateInfos = quesStateDao.queryQuesStateInfo(stateEntity.getId());
            //查询答案
            for (FunctionTreeCaseStateEvaluateInfo evaluateInfo : evaluateInfos) {
                List<QuesStateOptionInfoVO> optionsInfo = quesStateOptionsDao.findOptionsInfo(evaluateInfo.getId());
                evaluateInfo.setOptions(optionsInfo);
            }
            stateResponse.setEvaluateInfo(evaluateInfos);
        }else {
            //拆线呢功能走查
            List<FunctionTreeCaseStateCheckInfo> processStateInfo = processStateDao.findProcessStateInfo(stateEntity.getId());
            for (FunctionTreeCaseStateCheckInfo checkInfo : processStateInfo) {
                //如果为0就不用查询选项
                if ("1".equals(checkInfo.getOptionsType())){
                    List<TestProcessOptionsStateVO> processStateOptionInfo = processStateOptionsDao.findProcessStateOptionInfo(checkInfo.getId());
                    checkInfo.setOptions(processStateOptionInfo);
                }
            }
            stateResponse.setCheckInfo(processStateInfo);
        }
        return stateResponse;
    }


    /**
     * 自定义数据转化为预设
     */
    @Override
    public void caseStateOptionSwitch(CaseStateOptionSwitchRequest request) {
        switch (request.getTestCaseType()){
            //功能评价
            case  0->{
                QuesStateEntity quesStateEntity = quesStateDao.selectById(request.getId());
                if (Objects.isNull(quesStateEntity)){
                    throw new ServerException("查询功能评价测试数据不存在");
                }
                String other = quesStateEntity.getOther();
                log.info("其他的选项为：{}",other);
                //查询其他state是否有重复的其他选项
                List<QuesStateEntity> quesStateEntities = quesStateDao.selectList(Wrappers.<QuesStateEntity>lambdaQuery()
                        .eq(QuesStateEntity::getOther, other)
                        .ne(QuesStateEntity::getId, quesStateEntity.getId())
                );
                quesStateEntities.add(quesStateEntity);
                //添加选项
                List<TestQuesOptionsEntity> optionsEntities = quesStateEntities.stream().map(it -> {
                    //查询排序大小
                    Integer maxSort = testQuesOptionsDao.findMaxSort(it.getQuesId());
                    TestQuesOptionsEntity entity = new TestQuesOptionsEntity();
                    entity.setOptions(other);
                    entity.setQuesId(it.getQuesId());
                    entity.setSort(++maxSort);
                    return entity;
                }).toList();
                testQuesOptionsDao.insert(optionsEntities);
                //清空other选项 并停驾
                Map<Integer, Integer> collect = optionsEntities.stream().collect(
                        Collectors.toMap(TestQuesOptionsEntity::getQuesId, TestQuesOptionsEntity::getId));
                //添加state选项
                List<QuesStateOptionsEntity> optionsStates = quesStateEntities.stream().map(it -> {
                    Integer optionId = collect.get(it.getQuesId());
                    if (Objects.nonNull(optionId)) {
                        QuesStateOptionsEntity options = new QuesStateOptionsEntity();
                        options.setQuesStateId(it.getId());
                        options.setSelectId(optionId);
                        return options;
                    }
                    return null;
                }).filter(Objects::nonNull).toList();
                quesStateOptionsDao.insert(optionsStates);
                //清空other选项
                List<Integer> ids = quesStateEntities.stream().map(QuesStateEntity::getId).toList();
                quesStateDao.update(Wrappers.<QuesStateEntity>lambdaUpdate()
                        .set(QuesStateEntity::getOther,null)
                        .in(QuesStateEntity::getId,ids)
                );
            }
            //功能走查
            case 1->{
                ProcessStateEntity processStateEntity = processStateDao.selectById(request.getId());
                if (Objects.isNull(processStateEntity)){
                    throw new ServerException("查询功能走查测试数据不存在");
                }
                String other = processStateEntity.getOther();
                log.info("其他的选项为：{}",other);
                //查询其他state是否有重复的其他选项
                List<ProcessStateEntity> processStateEntities = processStateDao.selectList(Wrappers.<ProcessStateEntity>lambdaQuery()
                        .eq(ProcessStateEntity::getOther, other)
                        .ne(ProcessStateEntity::getId, processStateEntity.getId())
                );
                processStateEntities.add(processStateEntity);
                //添加选项
                List<TestProcessOptionsEntity> optionsEntities = processStateEntities.stream().map(it -> {
                    //查询排序大小
                    Integer maxSort = testProcessOptionsDao.findMaxSort(it.getProcessId());
                    TestProcessOptionsEntity entity = new TestProcessOptionsEntity();
                    entity.setOptions(other);
                    entity.setProcessId(it.getProcessId());
                    entity.setSort(++maxSort);
                    return entity;
                }).toList();
                testProcessOptionsDao.insert(optionsEntities);
                //清空other选项 并停驾
                Map<Integer, Integer> collect = optionsEntities.stream().collect(
                        Collectors.toMap(TestProcessOptionsEntity::getProcessId, TestProcessOptionsEntity::getId));
                //添加state选项
                List<ProcessStateOptionsEntity> optionsStates = processStateEntities.stream().map(it -> {
                    Integer optionId = collect.get(it.getProcessId());
                    if (Objects.nonNull(optionId)) {
                        ProcessStateOptionsEntity options = new ProcessStateOptionsEntity();
                        options.setProcessStateId(it.getId());
                        options.setSelectId(optionId);
                        return options;
                    }
                    return null;
                }).filter(Objects::nonNull).toList();
                processStateOptionsDao.insert(optionsStates);
                //清空other选项
                List<Integer> ids = processStateEntities.stream().map(ProcessStateEntity::getId).toList();
                processStateDao.update(Wrappers.<ProcessStateEntity>lambdaUpdate()
                        .set(ProcessStateEntity::getOther,null)
                        .in(ProcessStateEntity::getId,ids)
                );

            }
        }
    }


    /**
     * 测试用例状态选项编辑
     * @param request
     */
    @Override
    public void caseStateOptionEdit(CaseStateOptionEditRequest request) {
        switch (request.getCaseType()){
            case  0->{
                QuesStateEntity quesStateEntity = quesStateDao.selectById(request.getId());
                if (Objects.isNull(quesStateEntity)){
                    throw new ServerException("查询功能评价测试数据不存在");
                }
                if (CollectionUtils.isNotEmpty(request.getOptions())){
                    List<CaseStateOptionEditOptionInfo> options = request.getOptions();
                    List<QuesStateOptionsEntity> list = options.stream().map(it -> {
                        QuesStateOptionsEntity entity = new QuesStateOptionsEntity();
                        entity.setId(it.getId());
                        entity.setSelectId(it.getSelectId());
                        entity.setDataState(it.getDataState());
                        entity.setQuesStateId(quesStateEntity.getId());
                        return entity;
                    }).toList();
                    quesStateOptionsDao.insertOrUpdate(list);
                }
                quesStateDao.update(Wrappers.<QuesStateEntity>lambdaUpdate()
                        .set(QuesStateEntity::getOther,request.getOther())
                        .eq(QuesStateEntity::getId,quesStateEntity.getId())
                );
            }
            case 1->{
                ProcessStateEntity processStateEntity = processStateDao.selectById(request.getId());
                if (Objects.isNull(processStateEntity)){
                    throw new ServerException("查询功能走查测试数据不存在");
                }
                if (CollectionUtils.isNotEmpty(request.getOptions())){
                    List<CaseStateOptionEditOptionInfo> options = request.getOptions();
                    List<ProcessStateOptionsEntity> list = options.stream().map(it -> {
                        ProcessStateOptionsEntity entity = new ProcessStateOptionsEntity();
                        entity.setId(it.getId());
                        entity.setSelectId(it.getSelectId());
                        entity.setDataState(it.getDataState());
                        entity.setProcessStateId(processStateEntity.getId());
                        return entity;
                    }).toList();
                    processStateOptionsDao.insertOrUpdate(list);
                }
//                processStateEntity.setOther(request.getOther());
//                processStateEntity.setErrorSelect(request.getErrorSelect());
//                processStateEntity.setError(request.getError());
//                processStateDao.insertOrUpdate(processStateEntity);
                processStateDao.update(Wrappers.<ProcessStateEntity>lambdaUpdate()
                        .set(ProcessStateEntity::getOther,request.getOther())
                        .set(ProcessStateEntity::getErrorSelect,request.getErrorSelect())
                        .set(ProcessStateEntity::getError,request.getError())
                        .eq(ProcessStateEntity::getId,processStateEntity.getId())
                );
            }

        }


    }


    /**
     * 修改测试用例错误
     * @param request
     */
    @Override
    public void caseStateErrorTypeEdit(TestCaseStateErrorRequest request) {
        TestPlatformVehicleTestStateEntity testStateEntity = testPlatformVehicleTestStateDao.selectById(request.getStateId());
        if (Objects.isNull(testStateEntity)){
            throw new ServerException("查询测试状态为空");
        }
//        testStateEntity.setErrorType(request.getErrorType());
//        testStateEntity.setErrorDetail(request.getErrorDetail());
        testPlatformVehicleTestStateDao.update(Wrappers.<TestPlatformVehicleTestStateEntity>lambdaUpdate()
                .set(TestPlatformVehicleTestStateEntity::getErrorType,request.getErrorType())
                .set(TestPlatformVehicleTestStateEntity::getErrorDetail,request.getErrorDetail())
                .eq(TestPlatformVehicleTestStateEntity::getId,testStateEntity.getId())
        );
//        switch (request.getCaseType()){
//            case 0 -> {
//
//            }
//            case 1 ->{
//                ProcessStateEntity processStateEntity = processStateDao.selectById(request.getStateId());
//                if (Objects.isNull(processStateEntity)){
//                    throw new ServerException("查询测试状态为空");
//                }
//                processStateEntity.setErrorSelect(request.getErrorType());
//                processStateEntity.setError(request.getErrorDetail());
//                processStateDao.insertOrUpdate(processStateEntity);
//            }
//        }
    }


    /**
     * 测试状态编辑
     * test_state update
     * @param request
     */
    @Override
    public void testCaseStateEdit(TestCaseStateEditRequest request) {
        TestPlatformVehicleTestStateEntity testStateEntity = testPlatformVehicleTestStateDao.selectById(request.getTestStateId());
        if (Objects.isNull(testStateEntity)){
            throw new ServerException("查询测试状态为空");
        }
        testStateEntity.setMaterialState(request.getMaterialState());
        testPlatformVehicleTestStateDao.insertOrUpdate(testStateEntity);
    }


    /**
     * 查询对应问题的选项
     * @return
     */
    @Override
    public List<TestCaseOptionsInfoResponse> findOptions(TestCaseOptionsInfoRequest request) {
        return switch (request.getCaseType()) {
            case 0 -> {
                List<TestQuesOptionsEntity> quesOptionsEntities = testQuesOptionsDao.selectList(Wrappers.<TestQuesOptionsEntity>lambdaQuery()
                        .eq(TestQuesOptionsEntity::getQuesId, request.getQuestionId())
                );
                yield  quesOptionsEntities.stream().map(it -> {
                    TestCaseOptionsInfoResponse infoResponse = new TestCaseOptionsInfoResponse();
                    infoResponse.setId(it.getId());
                    infoResponse.setOption(it.getOptions());
                    infoResponse.setSort(it.getSort());
                    return infoResponse;
                }).toList();
            }
            case 1 ->{
                List<TestProcessOptionsEntity> optionsEntities = testProcessOptionsDao.selectList(Wrappers.<TestProcessOptionsEntity>lambdaQuery()
                        .eq(TestProcessOptionsEntity::getProcessId, request.getQuestionId())
                );
                yield  optionsEntities.stream().map(it -> {
                    TestCaseOptionsInfoResponse infoResponse = new TestCaseOptionsInfoResponse();
                    infoResponse.setId(it.getId());
                    infoResponse.setOption(it.getOptions());
                    infoResponse.setSort(it.getSort());
                    return infoResponse;
                }).toList();
            }
            default -> new ArrayList<>();
        };
    }

    @Override
    public List<ErrorTypeVO> findErrorType(Integer caseType) {
        List<ErrorTypeEntity> errorTypeEntities = errorTypeDao.selectList(Wrappers.<ErrorTypeEntity>lambdaQuery()
                .eq(ErrorTypeEntity::getErrorType,caseType.equals(0)?1:2)
        );
        return ErrorTypeConvert.INSTANCE.convertList(errorTypeEntities);
    }


    /**
     * 查询评分问题 选项题目等
     * @return
     */
    @Override
    public List<FunctionTreeScoreQuestionResponse> findScoreQuestion(String functionTag) {
        //根据 功能标签tag 查询下面所有的选项
        List<TestQuesEntity> testQuesEntities = testQuesDao.selectList(Wrappers.<TestQuesEntity>lambdaQuery()
                .eq(TestQuesEntity::getTestName, functionTag)
                .orderByDesc(TestQuesEntity::getId)
        );
        if (CollectionUtils.isEmpty(testQuesEntities)){
            log.info("根据功能标签查询出的tag为空：{}",functionTag);
            return Collections.emptyList();
        }
        //查询题目
        List<FunctionTreeScoreQuestionResponse> responseList = testQuesEntities.stream().map(it -> {
            List<TestQuesOptionsEntity> quesOptionsEntities = testQuesOptionsDao.selectList(Wrappers.<TestQuesOptionsEntity>lambdaQuery()
                    .eq(TestQuesOptionsEntity::getQuesId, it.getId())
                    .orderByAsc(TestQuesOptionsEntity::getSort)
            );
            //转换
            FunctionTreeScoreQuestionResponse response = TestQuesConvert.INSTANCE.convertRes(it);
            List<TestQuesOptionsVO> testQuesOptionsVOS = TestQuesOptionsConvert.INSTANCE.convertList(quesOptionsEntities);
            response.setOptions(testQuesOptionsVOS);
            return response;
        }).toList();
        return responseList;
    }


    /**
     * 评分问题选项编辑
     */
    @Override
    public FunctionTreeScoreQuestionResponse editScoreQuestion(FunctionTreeScoreQuestionEditRequest request) {
        //查询问题id是否存在
        TestQuesEntity testQuesEntity = testQuesDao.selectById(request.getId());
        if (Objects.isNull(testQuesEntity)){
            throw new ServerException("数据不存在或已删除");
        }
        testQuesEntity.setOptionsType(request.getOptionsType());
        testQuesEntity.setCategoryType(request.getCategoryType());
        testQuesEntity.setState(request.getState());
        //查询该问题下的所有选项
        List<TestQuesOptionsEntity> optionsEntities = testQuesOptionsDao.selectList(Wrappers.<TestQuesOptionsEntity>lambdaQuery()
                .eq(TestQuesOptionsEntity::getQuesId, request.getId()));
        //转换选项为entity
        List<TestQuesOptionsVO> options = request.getOptions();
        if (CollectionUtils.isEmpty(options)){
            throw new ServerException("问题选项不能为空");
        }
        //判断需要删除的选项
        List<Integer> newOptionIds = options.stream().filter(it -> Objects.nonNull(it.getId())).map(TestQuesOptionsVO::getId).toList();
        List<Integer> deleteIds = optionsEntities.stream().filter(it -> !newOptionIds.contains(it.getId())).map(TestQuesOptionsEntity::getId).toList();
        log.info("需要删除的选项id为：{}",deleteIds);
        if (!CollectionUtils.isEmpty(deleteIds)){
            //删除选项
            testQuesOptionsDao.deleteByIds(deleteIds);
        }

        //保存新的
        List<TestQuesOptionsEntity> quesOptionsEntities = options.stream().map(it -> TestQuesOptionsConvert.INSTANCE.convertEntity(it, request.getId())).toList();
        testQuesOptionsDao.insertOrUpdate(quesOptionsEntities);
        testQuesDao.insertOrUpdate(testQuesEntity);
        FunctionTreeScoreQuestionResponse response = TestQuesConvert.INSTANCE.convertRes(testQuesEntity);
        List<TestQuesOptionsVO> testQuesOptionsVOS = TestQuesOptionsConvert.INSTANCE.convertList(quesOptionsEntities);
        response.setOptions(testQuesOptionsVOS);
        return response;
    }


    /**
     * 评分问题添加
     * @param request
     */
    @Override
    public void addScoreQuestion(FunctionTreeScoreQuestionAddRequest request) {
        //转换entity类
        TestQuesEntity entity = TestQuesConvert.INSTANCE.convert(request);
        log.info("保存的评分问题数据为：{}",JsonUtils.toJsonString(entity));
        testQuesDao.insert(entity);
        List<TestQuesOptionsVO> options = request.getOptions();
        //保存新的选项
        //保存新的
        List<TestQuesOptionsEntity> quesOptionsEntities = options.stream().map(it -> TestQuesOptionsConvert.INSTANCE.convertEntity(it,entity.getId())).toList();
        testQuesOptionsDao.insert(quesOptionsEntities);
    }

    /**
     * 评分编辑和新增
     * @param request
     */
    @Transactional
    @Override
    public void scoreQuestionUpdate(List<FunctionTreeScoreQuestionResponse> request) {
        if (CollectionUtils.isEmpty(request)){
            throw new ServerException("功能树评分不能为空");
        }
        for (FunctionTreeScoreQuestionResponse it : request) {
            //转换成entity
            TestQuesEntity convert = TestQuesConvert.INSTANCE.convert(it);
            testQuesDao.insertOrUpdate(convert);
            //获取当前所有的选项
            if (Objects.nonNull(convert.getId())) {
                List<TestQuesOptionsEntity> optionsEntities = testQuesOptionsDao.selectList(Wrappers.<TestQuesOptionsEntity>lambdaQuery()
                        .eq(TestQuesOptionsEntity::getQuesId, it.getId())
                );
                List<TestQuesOptionsVO> options = it.getOptions();
                if (CollectionUtils.isEmpty(options) && !CollectionUtils.isEmpty(optionsEntities)) {
                    List<Integer> entityIds = optionsEntities.stream().map(TestQuesOptionsEntity::getId).toList();
                    testQuesOptionDelete(it, entityIds);
                }
                //获取删除的选项
                if (!CollectionUtils.isEmpty(options) && !CollectionUtils.isEmpty(optionsEntities)) {
                    List<Integer> optionId = options.stream().filter(item -> Objects.nonNull(it.getId())).map(TestQuesOptionsVO::getId).toList();
                    if (!CollectionUtils.isEmpty(optionId)) {
                        List<Integer> deleteOptions = optionsEntities.stream().map(TestQuesOptionsEntity::getId).filter(id -> !optionId.contains(id)).toList();
                        testQuesOptionDelete(it, deleteOptions);
                    }
                }
                List<TestQuesOptionsEntity> saveUpdate = TestQuesOptionsConvert.INSTANCE.convertListEntity(options);
                if (!CollectionUtils.isEmpty(saveUpdate)) {
                    saveUpdate.forEach(item->item.setQuesId(convert.getId()));
                    testQuesOptionsDao.insertOrUpdate(saveUpdate);
                }
            }
        }
    }

    private void testQuesOptionDelete(FunctionTreeScoreQuestionResponse it, List<Integer> entityIds) {
        if (CollectionUtils.isEmpty(entityIds)){
            log.info("数据为空不进行删除");
            return;
        }
        //删除现有的所有选项
        int delete = testQuesOptionsDao.delete(Wrappers.<TestQuesOptionsEntity>lambdaQuery()
                .in(TestQuesOptionsEntity::getId, entityIds)
        );
        //删除测试数据所选的数据
        int stateDelete = quesStateOptionsDao.delete(Wrappers.<QuesStateOptionsEntity>lambdaQuery()
                .in(QuesStateOptionsEntity::getSelectId, entityIds)
        );
        log.info("testQuesOptionId：{},删除选项数量：{},删除测试数据：{}", it.getId(),delete,stateDelete);
    }
}
