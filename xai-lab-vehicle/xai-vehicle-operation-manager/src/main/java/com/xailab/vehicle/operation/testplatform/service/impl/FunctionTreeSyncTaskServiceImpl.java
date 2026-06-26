package com.xailab.vehicle.operation.testplatform.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncCaseInfoDto;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncInfoRequest;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncRequest;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeListResponse;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeTaskSyncJournalResponse;
import com.xailab.vehicle.feign.vehicledata.FunctionThreeTagFeign;
import com.xailab.vehicle.feign.vehicledata.VehiclePcafeRelevancyFunctionThreeTagFeign;
import com.xailab.vehicle.feign.vehicledata.VehicleTreeTaskFeign;
import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.framework.common.exception.ServerException;
import com.xailab.vehicle.framework.common.utils.JsonUtils;
import com.xailab.vehicle.framework.common.utils.RandomTraceUtils;
import com.xailab.vehicle.framework.security.user.SecurityUser;
import com.xailab.vehicle.framework.security.user.UserDetail;
import com.xailab.vehicle.operation.system.service.SysUserService;
import com.xailab.vehicle.operation.system.vo.SysUserVO;
import com.xailab.vehicle.operation.testplatform.convert.*;
import com.xailab.vehicle.operation.testplatform.dao.*;
import com.xailab.vehicle.operation.testplatform.entity.*;
import com.xailab.vehicle.operation.testplatform.enums.*;
import com.xailab.vehicle.operation.testplatform.feign.VehicleTreeTaskTestFeign;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncCreateRequest;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncTaskAuditRequest;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncTaskInfoRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.*;
import com.xailab.vehicle.operation.testplatform.service.SyncTaskOperationJournalService;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformFunctionTreeService;
import com.xailab.vehicle.operation.testplatform.vo.*;
import jakarta.annotation.Resource;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeSyncTaskQuery;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreeSyncTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能树数据同步表
 *
 * @author mumu
 * @since 1.0.0 2025-06-02
 */
@Service
@Slf4j
@DS("test_platform")
public class FunctionTreeSyncTaskServiceImpl extends BaseServiceImpl<FunctionTreeSyncTaskDao, FunctionTreeSyncTaskEntity> implements FunctionTreeSyncTaskService {
    @Resource
    private FunctionTreeSyncTaskInfoDao functionTreeSyncTaskInfoDao;

    @Resource
    private TestPlatformVehicleTestCaseDao  testPlatformVehicleTestCaseDao;

    @Resource
    private FunctionTreeStateDao functionTreeStateDao;

    @Resource
    private FunctionTreeSyncAuditJournalDao functionTreeSyncAuditJournalDao;

    @Resource
    private FunctionThreeTagFeign functionThreeTagFeign;

    @Resource
    private VehiclePcafeRelevancyFunctionThreeTagFeign vehiclePcafeRelevancyFunctionThreeTagFeign;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private VehicleTreeTaskFeign vehicleTreeTaskFeign;

    @Resource
    private VehicleTreeTaskTestFeign vehicleTreeTaskTestFeign;

    @Resource
    private TestPlatformVehicleTestStateDao testPlatformVehicleTestStateDao;

    @Resource
    private TestCaseMaterialDao testCaseMaterialDao;

    @Resource
    private TestPlatformFunctionTreeService testPlatformFunctionTreeService;

    @Resource
    private FunctionTreeSyncTaskJournalDao functionTreeSyncTaskJournalDao;

    @Resource
    private SyncTaskOperationJournalService syncTaskOperationJournalService;

    @Resource
    private SyncTaskOperationJournalDao syncTaskOperationJournalDao;

    @Resource
    private  QuesStateDao quesStateDao;

    @Resource
    private QuesStateOptionsDao quesStateOptionsDao;

    @Resource
    private  ProcessStateDao processStateDao;

    @Resource
    private ProcessStateOptionsDao processStateOptionsDao;

    @Resource
    private ErrorTypeDao errorTypeDao;

    @Value("${test_platform.function-tree.defaultScenarioId:6}")
    private Integer defaultScenarioId;

    @Value("${test_platform.function-tree.defaultScenarioTask:功能走查}")
    private String defaultScenarioTask;

    @Override
    public PageResult<FunctionTreeSyncTaskVO> page(FunctionTreeSyncTaskQuery query) {
        IPage<FunctionTreeSyncTaskEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        List<Long> userIds = new ArrayList<>();
        for (FunctionTreeSyncTaskEntity record : page.getRecords()) {
            userIds.add(record.getCreator());
            userIds.add(record.getUpdater());
        }
        Map<Long, SysUserVO> userMap = sysUserService.getUserMap(userIds);
        List<FunctionTreeSyncTaskVO> list = page.getRecords().stream().map(it -> {
            FunctionTreeSyncTaskVO functionTreeSyncTaskVO = FunctionTreeSyncTaskConvert.INSTANCE.convert(it);
            functionTreeSyncTaskVO.setCreatorName(userMap.get(it.getCreator()).getUsername());
            functionTreeSyncTaskVO.setUpdaterName(userMap.get(it.getUpdater()).getUsername());
            return functionTreeSyncTaskVO;
        }).toList();
        return new PageResult<>(list, page.getTotal());
    }


    /**
     * 查询审核记录分页列表
     * taskSerial 任务编号
     */
    @Override
    public List<FunctionTreeSyncAuditJournalVO> findAuditJournalList(String taskSerial) {
        List<FunctionTreeSyncAuditJournalEntity> functionTreeSyncAuditJournalEntities = functionTreeSyncAuditJournalDao.selectList(Wrappers.<FunctionTreeSyncAuditJournalEntity>lambdaQuery()
                .eq(FunctionTreeSyncAuditJournalEntity::getTaskSerial, taskSerial));
        return FunctionTreeSyncTaskConvert.INSTANCE.convertAuditJournalList(functionTreeSyncAuditJournalEntities);
    }


    /**
     * 查询function tree全部数据 已经对应是否选中
     * @param testRecordId
     * @param syncRule
     * @return
     */
    @Deprecated
    @Override
    public List<FunctionTreeSyncTreeListResponse> findFunctionTreeList(Integer testRecordId,Integer syncRule,String taskSerial) {
        //查询数据
        List<TestPlatformFunctionTreeVo> records = testPlatformVehicleTestCaseDao.selectFunctionTreeTagList(6);
        List<FunctionTreeSyncTreeListResponse> responseList = records.stream().map(it -> {
            FunctionTreeSyncTreeListResponse convert = TestPlatformVehicleTestCaseConvert.INSTANCE.convertRes(it);
            convert.setTagType(defaultScenarioTask.equals(convert.getScenarioTask()) ? 1 : 0);
            //根据场景任务获取测试用例
            List<FunctionTreeCaseVo> entities = testPlatformVehicleTestCaseDao.selectTreeCaseList(defaultScenarioId,
                    it.getTaskDetail(), it.getScenarioTask(), it.getFunctionId(), testRecordId);
            List<TestPlatformFunctionTreeCaseResponse> treeCaseResponses = entities.stream().map(item -> {
                TestPlatformFunctionTreeCaseResponse testPlatformFunctionTreeCaseResponse = TestPlatformVehicleTestCaseConvert.INSTANCE.convertRes(item, it);
                testPlatformFunctionTreeCaseResponse.setCaseType(defaultScenarioTask.equals(item.getScenarioTask()) ? 1 : 0);
                return testPlatformFunctionTreeCaseResponse;
            }).toList();
            //设置功能评价状态和数据状态
            FunctionTreeStateEntity stateEntity = functionTreeStateDao.selectOne(Wrappers.<FunctionTreeStateEntity>lambdaQuery()
                    .eq(FunctionTreeStateEntity::getTaskDetail, it.getTaskDetail())
                    .eq(FunctionTreeStateEntity::getScenarioTask, it.getScenarioTask())
                    .eq(FunctionTreeStateEntity::getFunctionDomainId, it.getFunctionId())
                    .eq(FunctionTreeStateEntity::getRecordId, testRecordId)
            );
            convert.setTestCaseInfo(treeCaseResponses);
            convert.setFunctionEvaluate(Objects.nonNull(stateEntity) ? stateEntity.getFunctionEvaluate() : FunctionTreeEvaluteStateEnum.UN.getValue());
            convert.setDataState(Objects.nonNull(stateEntity) ? stateEntity.getDataState() : FunctionTreeDataStateEnum.LACK_OF_DATA.getValue());
            return convert;
        }).toList();

        //设置目标功能标签
        if (Objects.nonNull(syncRule)){
            //目标功能标签处理
            //默认关联
            if (FunctionTreeTaskSyncRuleEnum.DEFAULT_LINK.equals(syncRule)){
                //获取默认关联数据
                List<PcafeRelevancyFunctionThreeTagVo> associationData = getAssociationData();
                if (!CollectionUtils.isEmpty(associationData)){
                    Map<String,String> roleRelevance = associationData.stream().
                            collect(Collectors.toMap(PcafeRelevancyFunctionThreeTagVo::getPecafeThreeNumber,
                                    PcafeRelevancyFunctionThreeTagVo::getBeeevalThreeNumber));
                    responseList.forEach(response ->  response.setTargetFunctionTag(roleRelevance.get(response.getFunctionTag())));
                }
            }
            if (FunctionTreeTaskSyncRuleEnum.FUNCTION_ID_LINK.equals(syncRule)){
                for (FunctionTreeSyncTreeListResponse response : responseList) {
                    //拆分出功能id
                    String[] split = response.getFunctionTag().split("-");
                    response.setTargetFunctionTag(split[0]);
                }
            }
        }
        //不为空查询详情
        if (StringUtils.isNotBlank(taskSerial)){
            List<FunctionTreeSyncTaskInfoListVO> functionTreeList = functionTreeSyncTaskInfoDao.findFunctionTreeList(taskSerial);
            Map<String, FunctionTreeSyncTaskInfoListVO> map = functionTreeList.stream().collect(Collectors.toMap(FunctionTreeSyncTaskInfoListVO::getFunctionTag, it -> it));
            responseList.forEach(response -> response.setTargetFunctionTag(map.get(response.getFunctionTag()).getTargetFunctionTag()));
        }
        return responseList;
    }


    /**
     * 查询 有映射关系的功能树结构数据
     * @param testRecordId
     * @param syncRule
     * @return
     */
    @Override
    public SyncTaskTreeResponse findMapTreeList(Integer testRecordId, Integer syncRule, String taskSerial) {
        //查询数据
        List<TestPlatformFunctionTreeVo> records = testPlatformVehicleTestCaseDao.selectFunctionTreeTagList(6);
        List<FunctionTreeListResponse> functionTreeList = functionThreeTagFeign.findFunctionTreeList();
        if (CollectionUtils.isEmpty(functionTreeList)){
            throw new ServerException("功能树数据为空");
        }
        Map<String, FunctionTreeListResponse> beeevalMap = functionTreeList.stream().collect(
                Collectors.toMap(FunctionTreeListResponse::getTagNumber, it->it));
        Map<String, TestPlatformFunctionTreeVo> peaceMap = records.stream().collect(
                Collectors.toMap(TestPlatformFunctionTreeVo::getTaskDetail, it -> it,  (a, b) -> a));

        //目标功能标签处理
        List<FunctionTreeSyncAllTreeResponse> responseList  = new ArrayList<>();
        //默认关联
        if (FunctionTreeTaskSyncRuleEnum.DEFAULT_LINK.equals(syncRule)){
            //获取默认关联数据
            List<PcafeRelevancyFunctionThreeTagVo> associationData = getAssociationData();
            if (!CollectionUtils.isEmpty(associationData)){
                responseList = associationData.stream().map(it -> {
                    TestPlatformFunctionTreeVo treeVo = peaceMap.get(it.getPecafeThreeNumber());
                    FunctionTreeListResponse response = beeevalMap.get(it.getBeeevalThreeNumber());
                    if (Objects.isNull(treeVo) || Objects.isNull(response)) {
                        return null;
                    }
                    FunctionTreeSyncAllTreeResponse convert = TestPlatformVehicleTestCaseConvert.INSTANCE.convertResV2(treeVo);
//                    convert.setTagType(defaultScenarioTask.equals(convert.getScenarioTask()) ? 1 : 0);
                    convert.setTagNumber(response.getTagNumber());
                    convert.setTagName(response.getTagName());
                    convert.setDescription(response.getDescription());
                    convert.setFunctionTreeCase(response.getFunctionTreeCase());
                    return convert;
                }).filter(Objects::nonNull).toList();
            }
        }
        if (FunctionTreeTaskSyncRuleEnum.FUNCTION_ID_LINK.equals(syncRule)){
            Set<String> strings = peaceMap.keySet();
            responseList =  strings.stream().map(it->{
                TestPlatformFunctionTreeVo treeVo = peaceMap.get(it);
                String[] split = it.split("-");
                String tagNumber = split[0];
                FunctionTreeListResponse response = beeevalMap.get(tagNumber);
                if (Objects.isNull(response)) {
                    return null;
                }
                FunctionTreeSyncAllTreeResponse convert = TestPlatformVehicleTestCaseConvert.INSTANCE.convertResV2(treeVo);
//                convert.setTagType(defaultScenarioTask.equals(convert.getScenarioTask()) ? 1 : 0);
                convert.setTagNumber(response.getTagNumber());
                convert.setTagName(response.getTagName());
                convert.setDescription(response.getDescription());
                convert.setFunctionTreeCase(response.getFunctionTreeCase());
                return convert;
            }).filter(Objects::nonNull).toList();
        }
        if (FunctionTreeTaskSyncRuleEnum.CUSTOM_LINK.equals(syncRule) && StringUtils.isNotBlank(taskSerial)){
            List<FunctionTreeSyncTaskInfoListVO> infoListVOS = functionTreeSyncTaskInfoDao.findFunctionTreeList(taskSerial);
            responseList = infoListVOS.stream().map(it -> {
                TestPlatformFunctionTreeVo treeVo = peaceMap.get(it.getFunctionTag());
                FunctionTreeListResponse response = beeevalMap.get(it.getTargetFunctionTag());
                if (Objects.isNull(treeVo) || Objects.isNull(response)) {
                    return null;
                }
                FunctionTreeSyncAllTreeResponse convert = TestPlatformVehicleTestCaseConvert.INSTANCE.convertResV2(treeVo);
//                convert.setTagType(defaultScenarioTask.equals(convert.getScenarioTask()) ? 1 : 0);
                convert.setTagNumber(response.getTagNumber());
                convert.setTagName(response.getTagName());
                convert.setDescription(response.getDescription());
                convert.setFunctionTreeCase(response.getFunctionTreeCase());
                return convert;
            }).toList();
        }
        setTreeState(testRecordId, responseList);
        //映射关系
        Map<String, String> mapping = responseList.stream().
                collect(Collectors.toMap(FunctionTreeSyncAllTreeResponse::getFunctionTag, FunctionTreeSyncAllTreeResponse::getTagName));
        Set<String> pcafeTag = mapping.keySet();
        Collection<String> beeevalTag = mapping.values();

        List<FunctionTreeSyncAllTreeResponse> noCorrespondPcafe = records.stream().filter(it -> !pcafeTag.contains(it.getTaskDetail())).map(it -> {
            FunctionTreeSyncAllTreeResponse convert = TestPlatformVehicleTestCaseConvert.INSTANCE.convertResV2(it);
//            convert.setTagType(defaultScenarioTask.equals(convert.getScenarioTask()) ? 1 : 0);
            return convert;
        }).toList();
        List<FunctionTreeSyncAllTreeResponse> noCorrespondBeeeval = functionTreeList.stream().filter(it -> !beeevalTag.contains(it.getTagName())).map(it -> {
            FunctionTreeSyncAllTreeResponse convert = new FunctionTreeSyncAllTreeResponse();
            convert.setTagNumber(it.getTagNumber());
            convert.setTagName(it.getTagName());
            convert.setDescription(it.getDescription());
            convert.setFunctionTreeCase(it.getFunctionTreeCase());
            return convert;
        }).toList();

        //设置功能树状态
        setTreeState(testRecordId, noCorrespondPcafe);
        if (FunctionTreeTaskSyncRuleEnum.CUSTOM_LINK.equals(syncRule)){
            SyncTaskTreeResponse taskTreeResponse = new SyncTaskTreeResponse();
            taskTreeResponse.setCorrespondTree(new ArrayList<>());

            ArrayList<FunctionTreeSyncAllTreeResponse> responses = new ArrayList<>();
            responses.addAll(responseList);
            responses.addAll(noCorrespondPcafe);
            taskTreeResponse.setNoCorrespondPcafeTree(responses);

            ArrayList<FunctionTreeSyncAllTreeResponse> responseBeeeval = new ArrayList<>();
            responseBeeeval.addAll(responseList);
            responseBeeeval.addAll(noCorrespondBeeeval);
            taskTreeResponse.setNoCorrespondBeeevalTree(responseBeeeval);
            return taskTreeResponse;
        }else {
            SyncTaskTreeResponse taskTreeResponse = new SyncTaskTreeResponse();
            taskTreeResponse.setCorrespondTree(responseList);
            taskTreeResponse.setNoCorrespondPcafeTree(noCorrespondPcafe);
            taskTreeResponse.setNoCorrespondBeeevalTree(noCorrespondBeeeval);
            return taskTreeResponse;
        }
    }

    /**
     * 设置功能树状态
     * @param testRecordId
     * @param responseList
     */
    private void setTreeState(Integer testRecordId, List<FunctionTreeSyncAllTreeResponse> responseList) {
        //设置状态
        responseList.forEach(it -> {
            //根据场景任务获取测试用例
            List<FunctionTreeCaseVo> entities = testPlatformVehicleTestCaseDao.selectTreeCaseList(defaultScenarioId,
                    it.getFunctionTag(),null,null, testRecordId);
            List<TestPlatformFunctionTreeCaseResponse> treeCaseResponses = entities.stream().map(item -> {
                TestPlatformFunctionTreeCaseResponse testPlatformFunctionTreeCaseResponse = TestPlatformVehicleTestCaseConvert.INSTANCE.convertRes(item);
//                testPlatformFunctionTreeCaseResponse.setCaseType(defaultScenarioTask.equals(item.getScenarioTask()) ? 1 : 0);
                return testPlatformFunctionTreeCaseResponse;
            }).toList();
            //设置功能评价状态和数据状态
            List<FunctionTreeStateEntity> stateEntityList = functionTreeStateDao.selectList(Wrappers.<FunctionTreeStateEntity>lambdaQuery()
                            .eq(FunctionTreeStateEntity::getTaskDetail, it.getFunctionTag())
//                    .eq(FunctionTreeStateEntity::getScenarioTask, it.getScenarioTask())
//                    .eq(FunctionTreeStateEntity::getFunctionDomainId, it.getFunctionId())
                            .eq(FunctionTreeStateEntity::getRecordId, testRecordId)
                            .eq(FunctionTreeStateEntity::getDeleted, 0)
            );
            FunctionTreeStateEntity stateEntity = CollectionUtils.isEmpty(stateEntityList)? null : stateEntityList.getFirst();
            it.setTestCaseInfo(treeCaseResponses);
            it.setFunctionEvaluate(Objects.nonNull(stateEntity) ? stateEntity.getFunctionEvaluate() : FunctionTreeEvaluteStateEnum.UN.getValue());
            it.setDataState(Objects.nonNull(stateEntity) ? stateEntity.getDataState() : FunctionTreeDataStateEnum.LACK_OF_DATA.getValue());
        });
    }

    private LambdaQueryWrapper<FunctionTreeSyncTaskEntity> getWrapper(FunctionTreeSyncTaskQuery query){
        LambdaQueryWrapper<FunctionTreeSyncTaskEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotBlank(query.getTaskSerial()), FunctionTreeSyncTaskEntity::getTaskSerial, query.getTaskSerial())
                .like(StringUtils.isNotBlank(query.getTaskName()), FunctionTreeSyncTaskEntity::getTaskName, "%"+query.getTaskName()+"%")
                .eq(StringUtils.isNotBlank(query.getTestRecordId()), FunctionTreeSyncTaskEntity::getTestRecordId, query.getTestRecordId())
                .eq(StringUtils.isNotBlank(query.getVehicleId()), FunctionTreeSyncTaskEntity::getVehicleId, query.getVehicleId())
                .eq(Objects.nonNull(query.getSyncRule()), FunctionTreeSyncTaskEntity::getSyncRule, query.getSyncRule())
                .eq(Objects.nonNull(query.getTaskState()), FunctionTreeSyncTaskEntity::getTaskState, query.getTaskState())
                .eq(Objects.nonNull(query.getCreator()), FunctionTreeSyncTaskEntity::getCreator, query.getCreator())
                .eq(FunctionTreeSyncTaskEntity::getDeleted, 0);
        return wrapper;
    }


    /**
     * 创建同步任务
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTask(FunctionTreeSyncCreateRequest request) {
        //生成对应任务流水号
        String traceId = RandomTraceUtils.getTraceId("FTAT");
        log.info("创建同步任务生成的流水号为：{}",traceId);
        Long userId = SecurityUser.getUserId();
        //创建任务
        FunctionTreeSyncTaskEntity convert = FunctionTreeSyncTaskConvert.INSTANCE.convert(request, traceId,userId);
        convert.setTaskState(FunctionTreeTaskStatusEnum.SYNC_PREVIEW.getCode());
        //判断选择的是那个规则
        Map<String, String> roleRelevance;
        if (FunctionTreeTaskSyncRuleEnum.DEFAULT_LINK.equals(request.getSyncRule())){
            //获取关联的数据
            List<PcafeRelevancyFunctionThreeTagVo> associationData = getAssociationData();
            if (CollectionUtils.isEmpty(associationData)){
                throw new ServerException("未找到关联数据");
            }
            roleRelevance = associationData.stream().
                    collect(Collectors.toMap(PcafeRelevancyFunctionThreeTagVo::getPecafeThreeNumber,
                            PcafeRelevancyFunctionThreeTagVo::getBeeevalThreeNumber));
        } else {
            roleRelevance = null;
        }
        List<FunctionTreeSyncTaskInfoEntity> infoEntities = request.getSyncTaskInfos().stream().map(it -> {
            FunctionTreeSyncTaskInfoEntity infoEntity = FunctionTreeSyncTaskInfoConvert.INSTANCE.convertEntity(it, traceId);
            if (FunctionTreeTaskSyncRuleEnum.DEFAULT_LINK.equals(request.getSyncRule()) && roleRelevance != null) {
                String targetFunctionTag = roleRelevance.get(it.getFunctionTag());
                if  (StringUtils.isBlank(targetFunctionTag)){
                    return null;
                }
                infoEntity.setTargetFunctionTag(targetFunctionTag);
            } else if (FunctionTreeTaskSyncRuleEnum.FUNCTION_ID_LINK.equals(request.getSyncRule())) {
                //拆分出功能id
                String[] split = it.getFunctionTag().split("-");
                infoEntity.setTargetFunctionTag(split[0]);
            } else if (FunctionTreeTaskSyncRuleEnum.CUSTOM_LINK.equals(request.getSyncRule())){
                infoEntity.setTargetFunctionTag(it.getTargetFunctionTag());
            }
            if (!CollectionUtils.isEmpty(it.getTestCaseId())) {
                infoEntity.setTestCaseId(String.join(",", it.getTestCaseId()));
            }
            //获取选项数据
            String syncInfoOptions = getSyncInfoOptions(it.getTestCaseId(), infoEntity, request.getTestRecordId());
            infoEntity.setSyncOption(syncInfoOptions);
            return infoEntity;
        }).filter(Objects::nonNull).toList();
        //保存任务
        baseMapper.insert(convert);
        //保存任务详情
        functionTreeSyncTaskInfoDao.insert(infoEntities);
        //设置任务状态
        for (FunctionTreeSyncTaskInfoEntity infoEntity : infoEntities) {
            testPlatformFunctionTreeService.updateFunctionTreeState(Integer.valueOf(request.getTestRecordId()),infoEntity.getFunctionTag(),FunctionTreeDataStateEnum.PENDING_REVIEW.getValue());

        }
        //记录操作日志
        syncTaskOperationJournalService.saveTaskOperationJournal(traceId, FunctionTreeTaskOperationEnum.CREATE_SYNC_TASK, true, "任务创建成功");
    }

    /**
     * 获取任务详情
     * @param infoEntity
     * @return
     */
    private String getSyncInfoOptions(List<String> testCaseId,FunctionTreeSyncTaskInfoEntity infoEntity,String testRecordId) {
//        List<String> testCaseId = request.getTestCaseId();
        if (CollectionUtils.isEmpty(testCaseId)){
            log.info("任务详情没有选择测试用例:{}",infoEntity.getFunctionTag());
            return null;
        }
        //查询所有的测试用例详情
        List<TestPlatformVehicleTestCaseEntity> testCaseEntities = testPlatformVehicleTestCaseDao.selectList(Wrappers.<TestPlatformVehicleTestCaseEntity>lambdaQuery()
                .in(TestPlatformVehicleTestCaseEntity::getId, testCaseId)
        );
        //查询出所有测试用例的test_state
        //查询state id
        List<TestPlatformVehicleTestStateEntity> stateEntityList = testPlatformVehicleTestStateDao.selectList(Wrappers.<TestPlatformVehicleTestStateEntity>lambdaQuery()
                .eq(TestPlatformVehicleTestStateEntity::getRecordId, testRecordId)
                .in(TestPlatformVehicleTestStateEntity::getTestcaseId, testCaseId)
        );
        if (CollectionUtils.isEmpty(stateEntityList)){
            log.info("任务详情没有找到对应的测试用例状态:{}",infoEntity.getFunctionTag());
            return null;
        }
        Map<Integer, TestPlatformVehicleTestStateEntity> testStateMap = stateEntityList.stream().collect(Collectors.toMap(TestPlatformVehicleTestStateEntity::getTestcaseId, it -> it));
        //转换成map
        String syncOptions = testCaseEntities.stream().map(testCaseEntity -> {
            TestPlatformVehicleTestStateEntity stateEntity = testStateMap.get(testCaseEntity.getId());
            if (Objects.isNull(stateEntity)) {
                log.info("任务详情没有找到对应的测试用例状态:{},testCaseId:{}", infoEntity.getFunctionTag(), testCaseEntity.getId());
                return null;
            }
            //判断是否测试
//            if (stateEntity.getIsSuccessful().equals(-1)) {
//                log.info("任务详情没有找到对应的测试用例状态:{},testCaseId:{}", infoEntity.getFunctionTag(), testCaseEntity.getId());
//                return null;
//            }
            //判断是否是功能走查还是功能评价
            if (!defaultScenarioTask.equals(testCaseEntity.getScenarioTask())) {
                log.info("该用例id为功能评价:{}", testCaseEntity.getId());
                //测试失败
                if (stateEntity.getIsSuccessful().equals(0) || Objects.nonNull(stateEntity.getErrorType())) {
                    //查询error详情
                    String errorName = null;
                    if (Objects.nonNull(stateEntity.getErrorType()) && !stateEntity.getErrorType().equals(6)){
                        ErrorTypeEntity errorTypeEntity = errorTypeDao.selectOne(Wrappers.<ErrorTypeEntity>lambdaQuery()
                                .eq(ErrorTypeEntity::getErrorType, 1)
                                .eq(ErrorTypeEntity::getId, stateEntity.getErrorType())
                        );
                        errorName = Objects.nonNull(errorTypeEntity)?errorTypeEntity.getErrorName():null;
                    }
                    if (StringUtils.isNotBlank(stateEntity.getErrorDetail())) {
                        errorName = StringUtils.isNotBlank(errorName) ? String.join(",", errorName,stateEntity.getErrorDetail()) :
                                stateEntity.getErrorDetail();
                    }
                    if (StringUtils.isBlank(errorName)){
                        log.info("该测试任务错误选项为空不进行拼接：{},tag:{}", stateEntity.getId(), infoEntity.getFunctionTag());
                        return null;
                    }
                    return String.join("_",String.valueOf(testCaseEntity.getId()),errorName);
                }
                //查询功能评价题目和id
                List<FunctionTreeCaseStateEvaluateInfo> evaluateInfos = quesStateDao.queryQuesStateInfo(stateEntity.getId());
                //查询答案
                String valueStr = evaluateInfos.stream().map(evaluateInfo -> {
                    List<QuesStateOptionInfoVO> optionsInfo = quesStateOptionsDao.findOptionsInfo(evaluateInfo.getId());
                    //TODO 选项去重
                    String optionsStr = optionsInfo.stream().map(QuesStateOptionInfoVO::getOptions).collect(Collectors.joining(","));
                    if (StringUtils.isNotBlank(evaluateInfo.getOther())) {
                        optionsStr = StringUtils.isNotBlank(optionsStr) ? String.join(",", optionsStr, evaluateInfo.getOther()) :
                                evaluateInfo.getOther();
                    }
                    return optionsStr;
                }).filter(StringUtils::isNotBlank).collect(Collectors.joining("-"));
                if (StringUtils.isBlank(valueStr)){
                    log.info("该测试任务题目选项为空不进行拼接：{},tag:{}", stateEntity.getId(), infoEntity.getFunctionTag());
                    return null;
                }
                return String.join("_",String.valueOf(testCaseEntity.getId()),valueStr);
            }


            //拆线呢功能走查
            log.info("该用例id为功能走查:{}", infoEntity.getFunctionTag());
            List<FunctionTreeCaseStateCheckInfo> processStateInfo = processStateDao.findProcessStateInfo(stateEntity.getId());
            String valueStr = processStateInfo.stream().map(checkInfo -> {
                if (Objects.isNull(checkInfo.getIsSuccess())) {
                    log.info("该用例测试结果为空或者为失败:{}", infoEntity.getFunctionTag());
                    return null;
                }
                //失败
//                if (checkInfo.getIsSuccess().equals(0) || Objects.nonNull(checkInfo.getErrorSelect())) {
                if (checkInfo.getIsSuccess().equals(0)) {
                    String errorName = null;
                    if (Objects.nonNull(checkInfo.getErrorSelect()) && !checkInfo.getErrorSelect().equals(4)){
                        ErrorTypeEntity errorTypeEntity = errorTypeDao.selectOne(Wrappers.<ErrorTypeEntity>lambdaQuery()
                                .eq(ErrorTypeEntity::getErrorType, 2)
                                .eq(ErrorTypeEntity::getId, checkInfo.getErrorSelect())
                        );
                        errorName = Objects.nonNull(errorTypeEntity)?errorTypeEntity.getErrorName():null;
                    }
                    if (StringUtils.isNotBlank(checkInfo.getError())) {
                        errorName = StringUtils.isNotBlank(errorName) ? String.join(",", errorName,checkInfo.getError()) :
                                checkInfo.getError();
                    }
                    return errorName;
                }

                if ("0".equals(checkInfo.getOptionsType())) {
                    log.info("任务详情没有选择功能走查选项:{}", infoEntity.getFunctionTag());
                    return null;
                }
                List<TestProcessOptionsStateVO> processStateOptionInfo = processStateOptionsDao.findProcessStateOptionInfo(checkInfo.getId());
                String optionsStr = processStateOptionInfo.stream().map(TestProcessOptionsStateVO::getOptions).collect(Collectors.joining(","));
                if (StringUtils.isNotBlank(checkInfo.getOther())) {
                    optionsStr = StringUtils.isNotBlank(optionsStr) ? String.join(",", optionsStr, checkInfo.getOther()) :
                             checkInfo.getOther();
                }
                return optionsStr;
            }).filter(StringUtils::isNotBlank).collect(Collectors.joining("-"));
            if (StringUtils.isBlank(valueStr)){
                log.info("该用例功能走查选项为空不进行拼接：{},tag:{}", stateEntity.getId(), infoEntity.getFunctionTag());
                return null;
            }
            return String.join("_",String.valueOf(testCaseEntity.getId()),valueStr);
        }).filter(StringUtils::isNotBlank).collect(Collectors.joining(";"));
        return syncOptions;
    }

    /**
     * 获取关联的数据
     * @return
     */
    private List<PcafeRelevancyFunctionThreeTagVo> getAssociationData() {
        List<PcafeRelevancyFunctionThreeTagEntity> threeTagEntityList = vehiclePcafeRelevancyFunctionThreeTagFeign.queryList();
        return threeTagEntityList.stream().map(it -> {
            PcafeRelevancyFunctionThreeTagVo threeTagVo = new PcafeRelevancyFunctionThreeTagVo();
            BeanUtils.copyProperties(it, threeTagVo);
            return threeTagVo;
        }).toList();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FunctionTreeSyncCreateRequest request) {
        UserDetail  user = SecurityUser.getUser();
        FunctionTreeSyncTaskEntity taskEntity = baseMapper.selectOne(Wrappers.<FunctionTreeSyncTaskEntity>lambdaQuery()
                .eq(FunctionTreeSyncTaskEntity::getTaskSerial, request.getTaskSerial())
                .eq(FunctionTreeSyncTaskEntity::getDeleted, 0)
        );
        if (Objects.isNull(taskEntity)) {
            throw new ServerException("同步任务不存在");
        }
        if (FunctionTreeTaskStatusEnum.APPROVED.equals(taskEntity.getTaskState())) {
            throw new ServerException("当前任务状态不能进行编辑");
        }
        List<FunctionTreeSyncTaskInfoEntity> syncTaskInfoList = functionTreeSyncTaskInfoDao.selectList(Wrappers.<FunctionTreeSyncTaskInfoEntity>lambdaQuery()
                .eq(FunctionTreeSyncTaskInfoEntity::getTaskSerial, request.getTaskSerial())
        );
        List<FunctionTreeSyncTaskInfoRequest> syncTaskInfos = request.getSyncTaskInfos();
        List<String> functionTags = syncTaskInfos.stream().map(FunctionTreeSyncTaskInfoRequest::getFunctionTag).toList();
        //需要删除的
        if (CollUtil.isNotEmpty(syncTaskInfoList)) {
            List<FunctionTreeSyncTaskInfoEntity> deleteEntity = syncTaskInfoList.stream().filter(syncTaskInfo -> !functionTags.contains(syncTaskInfo.getFunctionTag())).toList();
            //设置功能树状态
            //设置任务状态
            for (FunctionTreeSyncTaskInfoEntity infoEntity : deleteEntity) {
                testPlatformFunctionTreeService.updateFunctionTreeState(Integer.valueOf(request.getTestRecordId()),infoEntity.getFunctionTag(),FunctionTreeDataStateEnum.SYNCHRONIZATION.getValue());
            }
            //删除已选择的
            if (CollUtil.isNotEmpty(deleteEntity)) {
                List<Long> deleteIds = deleteEntity.stream().map(FunctionTreeSyncTaskInfoEntity::getId).toList();
                functionTreeSyncTaskInfoDao.deleteByIds(deleteIds);
                log.info("删除功能树同步任务详情：{}", deleteIds);
                syncTaskInfoList.removeAll(deleteEntity);
            }
        }
        //需要新增的
        List<String> infoEntityTag = syncTaskInfoList.stream().map(FunctionTreeSyncTaskInfoEntity::getFunctionTag).toList();
        List<FunctionTreeSyncTaskInfoEntity> insertEntity = syncTaskInfos.stream().filter(it -> !infoEntityTag.contains(it.getFunctionTag())).map(it -> {
            FunctionTreeSyncTaskInfoEntity convert = FunctionTreeSyncTaskInfoConvert.INSTANCE.convertEntity(it, taskEntity.getTaskSerial());
            if (CollUtil.isNotEmpty(it.getTestCaseId())) {
                convert.setTestCaseId(String.join(",", it.getTestCaseId()));
            }
            //设置功能状态
            testPlatformFunctionTreeService.updateFunctionTreeState(Integer.valueOf(request.getTestRecordId()),it.getFunctionTag(),FunctionTreeDataStateEnum.PENDING_REVIEW.getValue());
            return convert;
        }).toList();
        //编辑原有的数据
        syncTaskInfoList.forEach(it -> {
            for (FunctionTreeSyncTaskInfoRequest syncTaskInfo : syncTaskInfos) {
                if (it.getFunctionTag().equals(syncTaskInfo.getFunctionTag())) {
                    it.setTestCaseId(String.join(",", syncTaskInfo.getTestCaseId()));
                    break;
                }
            }
        });

        //判断同步规则是否改变
        if (taskEntity.getSyncRule().equals(request.getSyncRule())) {
            //设置目标tag
            setTargetFunctionTag(insertEntity,request.getSyncRule());
            syncTaskInfoList.addAll(insertEntity);
        }else {
            //设置目标tag
            if (FunctionTreeTaskSyncRuleEnum.CUSTOM_LINK.equals(request.getSyncRule())){
                Map<String, String> collect = syncTaskInfos.stream().collect(Collectors.toMap(FunctionTreeSyncTaskInfoRequest::getFunctionTag, FunctionTreeSyncTaskInfoRequest::getTargetFunctionTag));
                syncTaskInfoList.forEach(it -> it.setTargetFunctionTag(collect.get(it.getFunctionTag())));
            }
            syncTaskInfoList.addAll(insertEntity);
            //设置目标tag
            setTargetFunctionTag(syncTaskInfoList,request.getSyncRule());
        }
        syncTaskInfoList.forEach(it -> {
            if(StringUtils.isBlank(it.getTestCaseId())){
                return;
            }
            List<String> testCaseId = Arrays.stream(it.getTestCaseId().split(",")).toList();
            String syncInfoOptions = getSyncInfoOptions(testCaseId, it, request.getTestRecordId());
            it.setSyncOption(syncInfoOptions);
        });

        functionTreeSyncTaskInfoDao.insertOrUpdate(syncTaskInfoList);
        taskEntity.setTaskName(request.getTaskName());
        taskEntity.setDescription(request.getDescription());
        taskEntity.setTestRecordId(request.getTestRecordId());
        taskEntity.setVehicleId(request.getVehicleId());
        taskEntity.setSyncRule(request.getSyncRule());
        taskEntity.setUpdater(user.getId());
        if (FunctionTreeTaskStatusEnum.SYNC_PREVIEW.equals(taskEntity.getTaskState())||
                FunctionTreeTaskStatusEnum.SYNC_PREVIEW_SUCCESS.equals(taskEntity.getTaskState()) ||
                FunctionTreeTaskStatusEnum.SYNC_PREVIEW_FAILED.equals(taskEntity.getTaskState())){
            taskEntity.setTaskState(FunctionTreeTaskStatusEnum.SYNC_PREVIEW.getCode());
        }else {
            taskEntity.setTaskState(FunctionTreeTaskStatusEnum.UNDER_REVIEW.getCode());
        }
        baseMapper.updateById(taskEntity);
        log.info("更新功能树同步任务：{}", taskEntity);

        //记录日志
        syncTaskOperationJournalService.saveTaskOperationJournal(request.getTaskSerial(), FunctionTreeTaskOperationEnum.MODIFY_SYNC_TASK, true, "任务修改成功");
    }


    /**
     * 根据策略设置目标tag
     * @param insertEntity
     */
    private void setTargetFunctionTag(List<FunctionTreeSyncTaskInfoEntity> insertEntity, Integer syncRule) {

        //默认关联
        if (FunctionTreeTaskSyncRuleEnum.DEFAULT_LINK.equals(syncRule)){
            //获取默认关联数据
            List<PcafeRelevancyFunctionThreeTagVo> associationData = getAssociationData();
            if (CollectionUtils.isEmpty(associationData)){
                throw new ServerException("未找到关联数据");
            }
            Map<String,String> roleRelevance = associationData.stream().
                    collect(Collectors.toMap(PcafeRelevancyFunctionThreeTagVo::getPecafeThreeNumber,
                            PcafeRelevancyFunctionThreeTagVo::getBeeevalThreeNumber));
            for (FunctionTreeSyncTaskInfoEntity entity : insertEntity) {
                String targetFunctionTag = roleRelevance.get(entity.getFunctionTag());
                if (StringUtils.isNotBlank(targetFunctionTag)) {
                    entity.setTargetFunctionTag(targetFunctionTag);
                }
            }
        }
        if (FunctionTreeTaskSyncRuleEnum.FUNCTION_ID_LINK.equals(syncRule)){
            for (FunctionTreeSyncTaskInfoEntity entity : insertEntity) {
                //拆分出功能id
                String[] split = entity.getFunctionTag().split("-");
                entity.setTargetFunctionTag(split[0]);
            }
        }

//        if (FunctionTreeTaskSyncRuleEnum.CUSTOM_LINK.equals(syncRule)){
//            for (FunctionTreeSyncTaskInfoEntity entity : insertEntity) {
//                //拆分出功能id
//                entity.setTargetFunctionTag(entity.getTargetFunctionTag());
//            }
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> delete(List<Long> idList) {
        List<FunctionTreeSyncTaskEntity> entities = baseMapper.selectBatchIds(idList);
        if (CollUtil.isEmpty(entities)){
            log.info("删除对应详情为空：{}", idList);
            return Result.ok();
        }
        for (FunctionTreeSyncTaskEntity entity : entities) {
            if (entity.getTaskState().equals(FunctionTreeTaskStatusEnum.APPROVED.getCode())){
                log.info("任务为审核完成，不能删除：{}", entity);
                return Result.error("存在不可删除任务");
            }
        }

        for (FunctionTreeSyncTaskEntity entity : entities) {
            //回退同步数据
            Result<Void> voidResult = taskSyncFallback(entity.getTaskSerial());
            if (!voidResult.isOk()){
                log.error("回退同步数据失败：id:{},msg:{}",entity.getId(), voidResult.getMsg());
                return Result.error(voidResult.getMsg());
            }
            removeById(entity.getId());
            //记录日志
            syncTaskOperationJournalService.saveTaskOperationJournal(entity.getTaskSerial(), FunctionTreeTaskOperationEnum.DELETE_SYNC_TASK, true, "任务删除成功");
            //功能状态变更
            //获取任务详情
            List<FunctionTreeSyncTaskInfoEntity> syncTaskInfos = functionTreeSyncTaskInfoDao.selectList(Wrappers.<FunctionTreeSyncTaskInfoEntity>lambdaQuery()
                    .eq(FunctionTreeSyncTaskInfoEntity::getTaskSerial, entity.getTaskSerial())
            );
            for (FunctionTreeSyncTaskInfoEntity syncTaskInfo : syncTaskInfos) {
                testPlatformFunctionTreeService.updateFunctionTreeState(Integer.valueOf(entity.getTestRecordId()),syncTaskInfo.getFunctionTag(), FunctionTreeDataStateEnum.SYNCHRONIZATION.getValue());
            }
        }
        return Result.ok();
    }

    /**
     * 任务同步至镜像环境
     * @param taskSerial
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> syncTaskTest(String taskSerial) {
        UserDetail user = SecurityUser.getUser();
        log.info("同步镜像任务请求参数：{},操作人：{}",taskSerial,user.getUsername());
        FunctionTreeSyncTaskEntity taskEntity = baseMapper.selectOne(Wrappers.<FunctionTreeSyncTaskEntity>lambdaQuery()
                .eq(FunctionTreeSyncTaskEntity::getTaskSerial, taskSerial)
                .eq(FunctionTreeSyncTaskEntity::getDeleted, 0)
        );
        if (Objects.isNull(taskEntity)){
            throw new ServerException("同步任务不存在");
        }
        //如果不是待同步或者待同步失败
        if (!FunctionTreeTaskStatusEnum.SYNC_PREVIEW.equals(taskEntity.getTaskState()) &&
                !FunctionTreeTaskStatusEnum.SYNC_PREVIEW_FAILED.equals(taskEntity.getTaskState())) {
            throw new ServerException("当前任务状态不可同步镜像");
        }
        //操作记录日志
        Long operationId = syncTaskOperationJournalService.saveTaskOperationJournal(taskEntity.getTaskSerial(), FunctionTreeTaskOperationEnum.PREVIEW_SYNC_TASK,
                true, null);
        //同步
        functionTreeSync(taskEntity, "test", operationId);
        //记录状态
        taskEntity.setTaskState(FunctionTreeTaskStatusEnum.UNDER_REVIEW.getCode());
        taskEntity.setTaskMessage(null);
        taskEntity.setUpdater(user.getId());
        baseMapper.updateById(taskEntity);
        return Result.ok();
    }

    /**
     * 提交审核
     * @param taskSerial
     */
    @Override
    public Result<Void> commitAudit(String taskSerial) {
        UserDetail user = SecurityUser.getUser();
        log.info("提交任务审核请求参数：{},操作人：{}",taskSerial,user.getUsername());
        FunctionTreeSyncTaskEntity taskEntity = baseMapper.selectOne(Wrappers.<FunctionTreeSyncTaskEntity>lambdaQuery()
                .eq(FunctionTreeSyncTaskEntity::getTaskSerial, taskSerial)
                .eq(FunctionTreeSyncTaskEntity::getDeleted, 0)
        );
        if (Objects.isNull(taskEntity)){
            throw new ServerException("同步任务不存在");
        }
        //如果不是待同步或者待同步失败
        if (!FunctionTreeTaskStatusEnum.SYNC_PREVIEW_SUCCESS.equals(taskEntity.getTaskState())) {
            throw new ServerException("当前任务状态不可提交审核");
        }
        //操作记录日志
        syncTaskOperationJournalService.saveTaskOperationJournal(taskEntity.getTaskSerial(), FunctionTreeTaskOperationEnum.SUBMIT_AUDIT_SYNC_TASK,
                true, null);
        //记录状态
        taskEntity.setTaskState(FunctionTreeTaskStatusEnum.UNDER_REVIEW.getCode());
        taskEntity.setTaskMessage(null);
        taskEntity.setUpdater(user.getId());
        baseMapper.updateById(taskEntity);
        return Result.ok();
    }

    /**
     * 审核同步任务
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditSyncTask(FunctionTreeSyncTaskAuditRequest request) {
        UserDetail user = SecurityUser.getUser();
        log.info("审核同步任务请求参数：{},操作人：{}",request,user.getUsername());
        FunctionTreeSyncTaskEntity taskEntity = baseMapper.selectOne(Wrappers.<FunctionTreeSyncTaskEntity>lambdaQuery()
                .eq(FunctionTreeSyncTaskEntity::getTaskSerial, request.getTaskSerial())
                .eq(FunctionTreeSyncTaskEntity::getDeleted, 0)
        );
        if (Objects.isNull(taskEntity)){
            throw new ServerException("同步任务不存在");
        }
        //如果是审核失败保存
        if (!FunctionTreeTaskStatusEnum.UNDER_REVIEW.equals(taskEntity.getTaskState())) {
            throw new ServerException("当前任务状态不可审核");
        }
        //操作记录日志
        Long operationId = syncTaskOperationJournalService.saveTaskOperationJournal(taskEntity.getTaskSerial(), FunctionTreeTaskOperationEnum.AUDIT_SYNC_TASK,
                request.getAuditResult(), request.getAuditRemark());
        //同步功能树
        if (request.getAuditResult()){
            log.info("同步功能树 任务：{}",request.getTaskSerial());
            functionTreeSync(taskEntity,"prod",operationId);
        }else {
            log.info("同步功能审核失败更改功能树状态：{}",request.getTaskSerial());
            List<FunctionTreeSyncTaskInfoListVO> infoList = functionTreeSyncTaskInfoDao.findFunctionTreeList(taskEntity.getTaskSerial());
            for (FunctionTreeSyncTaskInfoListVO infoListVO : infoList) {
                testPlatformFunctionTreeService.updateFunctionTreeState(Integer.valueOf(taskEntity.getTestRecordId()),infoListVO.getFunctionTag(),FunctionTreeDataStateEnum.REJECTED.getValue());
            }
        }
        //记录状态
        taskEntity.setTaskState(request.getAuditResult()?FunctionTreeTaskStatusEnum.APPROVED.getCode():
                FunctionTreeTaskStatusEnum.REJECTED.getCode());
        taskEntity.setTaskMessage(request.getAuditRemark());
        taskEntity.setUpdater(user.getId());
        baseMapper.updateById(taskEntity);
    }


    /**
     * 查询已经选中的功能树数据
     * @param taskSerial
     * @return
     */
    @Override
    public FunctionTreeSyncSelectResponse findSelectList(String taskSerial) {
        List<FunctionTreeSyncTaskInfoListVO> list = functionTreeSyncTaskInfoDao.findFunctionTreeList(taskSerial);
        List<String> functionTagList = list.stream().map(FunctionTreeSyncTaskInfoListVO::getFunctionTag).toList();
        List<String> functionBeeevalTagList = list.stream().map(FunctionTreeSyncTaskInfoListVO::getTargetFunctionTag).toList();
        List<Integer> testCaseIdList = list.stream().map(FunctionTreeSyncTaskInfoListVO::getTestCaseId).filter(testCaseId ->!CollectionUtils.isEmpty(testCaseId)).flatMap(Collection::stream).toList();
        FunctionTreeSyncSelectResponse response = new FunctionTreeSyncSelectResponse();
        response.setFunctionTagList(functionTagList);
        response.setTestCaseIdList(testCaseIdList);
        response.setSelectBeeevalNumberList(functionBeeevalTagList);
        response.setSelectBeeevalCaseList(testCaseIdList);
        return response;
    }


    /**
     * 任务同步信息回滚
     * @param taskSerial
     */
    @Override
    public Result<Void> taskSyncFallback(String taskSerial) {
        //获取同步记录 journal
        List<FunctionTreeSyncTaskJournalEntity> journalEntities = functionTreeSyncTaskJournalDao.selectList(Wrappers.<FunctionTreeSyncTaskJournalEntity>lambdaQuery()
                .eq(FunctionTreeSyncTaskJournalEntity::getTaskSerial, taskSerial)
                .orderByDesc(FunctionTreeSyncTaskJournalEntity::getCreateTime)
        );
        if (CollectionUtils.isEmpty(journalEntities)){
            log.info("查询同步记录为空");
//            return Result.error("查询同步记录为空");
            return Result.ok();
        }
        for (FunctionTreeSyncTaskJournalEntity journalEntity : journalEntities) {
            String syncInfoJournal = journalEntity.getSyncInfoJournal();
            FunctionTreeTaskSyncJournalResponse response = JsonUtils.parseObject(syncInfoJournal, FunctionTreeTaskSyncJournalResponse.class);
            Result<Void> rollbackResult = null;
            if ("prod".equals(journalEntity.getEnvironment())){
                rollbackResult = vehicleTreeTaskFeign.taskSyncFallback(response);
            }else {
                rollbackResult = vehicleTreeTaskTestFeign.taskSyncFallback(response);
            }
            log.info("数据回滚结果：{},环境：{}，流水号：{}",rollbackResult,journalEntity.getEnvironment(),journalEntity.getTaskSerial());
            if (!rollbackResult.isOk()) {
                return rollbackResult;
            }
        }
        return Result.ok();
    }

    /**
     * 查询同步任务操作日志
     * @param taskSerial
     * @return
     */
    @Override
    public Result<List<SyncTaskOperationJournalVO>> selectOperationJournal(String taskSerial) {
        List<SyncTaskOperationJournalEntity> syncTaskOperationJournalEntities = syncTaskOperationJournalDao.selectList(Wrappers.<SyncTaskOperationJournalEntity>lambdaQuery()
                .eq(SyncTaskOperationJournalEntity::getTaskSerial, taskSerial)
        );
        return Result.ok(SyncTaskOperationJournalConvert.INSTANCE.convertList(syncTaskOperationJournalEntities));
    }


    /**
     * 查询同步任务信息
     * @param taskSerial
     * @param functionTag
     * @return
     */
    @Override
    public FunctionTreeSyncTaskInfoResponse selectSyncTaskInfo(String taskSerial, String functionTag) {
        FunctionTreeSyncTaskInfoEntity infoEntity = functionTreeSyncTaskInfoDao.selectOne(Wrappers.<FunctionTreeSyncTaskInfoEntity>lambdaQuery()
                .eq(FunctionTreeSyncTaskInfoEntity::getTaskSerial, taskSerial)
                .eq(FunctionTreeSyncTaskInfoEntity::getFunctionTag, functionTag)
        );
        FunctionTreeSyncTaskInfoResponse response = FunctionTreeSyncTaskInfoConvert.INSTANCE.convertRes(infoEntity);
        if (Objects.nonNull(response)){
            List<String> syncOptionList = new ArrayList<>();
            if (StringUtils.isNotBlank(infoEntity.getSyncOption())){
                String[] split = infoEntity.getSyncOption().split("-");
                syncOptionList.addAll(Arrays.asList(split));
            }
            response.setSyncOptionList(syncOptionList);
        }
        return response;
    }

    @Override
    public List<FunctionTreeSyncTaskCaseOptionResponse> selectSyncTaskCaseOption(String taskSerial, String functionTag) {
        FunctionTreeSyncTaskInfoEntity infoEntity = functionTreeSyncTaskInfoDao.selectOne(Wrappers.<FunctionTreeSyncTaskInfoEntity>lambdaQuery()
                .eq(FunctionTreeSyncTaskInfoEntity::getTaskSerial, taskSerial)
                .eq(FunctionTreeSyncTaskInfoEntity::getFunctionTag, functionTag)
        );
        if (Objects.nonNull(infoEntity)){
            String syncOption = infoEntity.getSyncOption();
            if (StringUtils.isNotBlank(syncOption)) {
                //拆分出每一个用例
                String[] split = syncOption.split(";");
                List<FunctionTreeSyncTaskCaseOptionResponse> list = Arrays.stream(split).map(it -> {
                    if (StringUtils.isBlank(it) || !it.contains("_")) {
                        return null;
                    }
                    String[] split1 = it.split("_");
                    FunctionTreeSyncTaskCaseOptionResponse response = new FunctionTreeSyncTaskCaseOptionResponse();
                    response.setTestCaseId(Integer.valueOf(split1[0]));
                    if (split1.length >= 2) {
                        String caseOption = split1[1];
                        String[] option = caseOption.split("-");
                        response.setSyncOptionList(Arrays.asList(option));
                    }

                    return response;
                }).filter(Objects::nonNull).toList();
                return list;
            }
            //设置已经勾选的
            String testCaseId = infoEntity.getTestCaseId();
            if (StringUtils.isNotBlank(testCaseId)) {
                String[] split = testCaseId.split(",");
                List<Integer> testCaseIdList = Arrays.stream(split).map(Integer::valueOf).toList();
                return testCaseIdList.stream().map(it -> {
                    FunctionTreeSyncTaskCaseOptionResponse response = new FunctionTreeSyncTaskCaseOptionResponse();
                    response.setTestCaseId(it);
                    response.setSyncOptionList(new ArrayList<>());
                    return response;
                }).toList();
            }
        }
        return new ArrayList<>();
    }


    /**
     * 编辑同步任务用例选项详细信息
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncTaskCaseOptionEdit(FunctionTreeSyncTaskCaseOptionEditRequest request) {
        String syncOption = request.getTestCaseSyncOptions().stream().map(it -> {
            if (CollectionUtils.isEmpty(it.getSyncOptionList())) {
                return null;
            }
            String caseSyncOption = String.join("-", it.getSyncOptionList());
            return String.join("_", String.valueOf(it.getTestCaseId()), caseSyncOption);
        }).filter(Objects::nonNull).collect(Collectors.joining(";"));
        int update = functionTreeSyncTaskInfoDao.update(Wrappers.<FunctionTreeSyncTaskInfoEntity>lambdaUpdate()
                .eq(FunctionTreeSyncTaskInfoEntity::getTaskSerial, request.getTaskSerial())
                .eq(FunctionTreeSyncTaskInfoEntity::getFunctionTag, request.getFunctionTag())
                .set(FunctionTreeSyncTaskInfoEntity::getSyncOption, syncOption)
        );
        log.info("更新同步任务信息结果：{},syncOption:{}",update,syncOption);
    }


    /**
     * 初始化同步任务用例选项数据
     */
    @Override
    public void initSyncTaskOption(String taskSerial) {
        //查询同步任务
        List<FunctionTreeSyncTaskEntity> syncTaskList = baseMapper.selectList(Wrappers.<FunctionTreeSyncTaskEntity>lambdaQuery()
                .ne(FunctionTreeSyncTaskEntity::getTaskState, FunctionTreeTaskStatusEnum.APPROVED.getCode())
                .eq(StringUtils.isNotBlank(taskSerial),FunctionTreeSyncTaskEntity::getTaskSerial, taskSerial)
        );
        if (CollectionUtils.isEmpty(syncTaskList)){
            log.info("查询同步任务为空");
            return;
        }
        for (FunctionTreeSyncTaskEntity taskEntity : syncTaskList) {
            log.info("初始化同步任务用例选项数据：{}",taskEntity.getTaskSerial());
            List<FunctionTreeSyncTaskInfoEntity> infoEntities = functionTreeSyncTaskInfoDao.selectList(Wrappers.<FunctionTreeSyncTaskInfoEntity>lambdaQuery()
                    .eq(FunctionTreeSyncTaskInfoEntity::getTaskSerial,taskEntity.getTaskSerial())
            );
            if (CollectionUtils.isEmpty(infoEntities)){
                log.info("查询同步任务信息为空:{}",taskEntity.getTaskSerial());
                continue;
            }
            //创建同步任务信息
            for (FunctionTreeSyncTaskInfoEntity infoEntity : infoEntities) {
                String testCaseId = infoEntity.getTestCaseId();
                List<String> testCaseIdList = Arrays.stream(testCaseId.split(",")).toList();
                String syncInfoOptions = getSyncInfoOptions(testCaseIdList, infoEntity, taskEntity.getTestRecordId());
                infoEntity.setSyncOption(syncInfoOptions);
            }
            log.info("更新同步任务信息：{},seria:{}",JsonUtils.toJsonString(infoEntities),taskEntity.getTaskSerial());
            functionTreeSyncTaskInfoDao.updateById(infoEntities);

            //重新同步镜像
            if (FunctionTreeTaskStatusEnum.SYNC_PREVIEW_SUCCESS.equals(taskEntity.getTaskState()) ||
                    FunctionTreeTaskStatusEnum.UNDER_REVIEW.equals(taskEntity.getTaskState()) ||
                    FunctionTreeTaskStatusEnum.REJECTED.equals(taskEntity.getTaskState())
            ){
                log.info("重新同步镜像：{},status:{}",taskEntity.getTaskSerial(),taskEntity.getTaskState());
                //操作记录日志
                Long operationId = syncTaskOperationJournalService.saveTaskOperationJournal(taskEntity.getTaskSerial(), FunctionTreeTaskOperationEnum.INIT_SYNC_TASK,
                        true, null);
                //同步
                functionTreeSync(taskEntity, "test", operationId);
            }

        }
    }

    /**
     * 编辑同步任务详细信息
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncTaskInfoEdit(FunctionTreeSyncTaskInfoEditRequest request) {
        String syncOption = null;
        if (!CollectionUtils.isEmpty(request.getSyncOptionList())){
            syncOption = String.join("-", request.getSyncOptionList());
        }
        int update = functionTreeSyncTaskInfoDao.update(Wrappers.<FunctionTreeSyncTaskInfoEntity>lambdaUpdate()
                .eq(FunctionTreeSyncTaskInfoEntity::getTaskSerial, request.getTaskSerial())
                .eq(FunctionTreeSyncTaskInfoEntity::getFunctionTag, request.getFunctionTag())
                .set(FunctionTreeSyncTaskInfoEntity::getSyncOption, syncOption)
        );
        log.info("更新同步任务信息结果：{}",update);
    }


    /**
     * TODO 同步功能树
     * @param taskEntity
     */
    private void functionTreeSync(FunctionTreeSyncTaskEntity taskEntity,String environment,Long operationId) {
        log.info("给定的同步数据环境为：{},操作记录id：{}",environment,operationId);
        //查询同步详情
        List<FunctionTreeSyncTaskInfoListVO> infoList = functionTreeSyncTaskInfoDao.findFunctionTreeList(taskEntity.getTaskSerial());
        //获取同步详情
        if (CollectionUtils.isEmpty(infoList)) {
            log.info("同步功能树任务详情为空：{}",taskEntity.getTaskSerial());
            throw new ServerException("同步功能树任务详情为空");
        }
        Result<List<FunctionTreeDataSyncInfoRequest>> syncInfoRequests = getSyncInfoRequests(taskEntity,infoList);
        //判断是否为空
        if (!syncInfoRequests.isOk() || CollectionUtils.isEmpty(syncInfoRequests.getData())){
            log.info("数据转换结果为空或错误：{}",syncInfoRequests);
            throw new ServerException("数据转换结果异常");
        }
        List<FunctionTreeDataSyncInfoRequest> infoRequests = syncInfoRequests.getData();

        FunctionTreeDataSyncRequest request = new FunctionTreeDataSyncRequest();
        request.setTaskSerial(taskEntity.getTaskSerial());
        request.setVehicleId(taskEntity.getVehicleId());
        request.setTestRecordId(taskEntity.getTestRecordId());
        request.setSyncInfo(infoRequests);
        log.info("数据转换结果：{}",JsonUtils.toJsonString(infoRequests));
        //发送feign
        Result<FunctionTreeTaskSyncJournalResponse>voidResult = null;
        if (environment.equals("prod")){
            voidResult =  vehicleTreeTaskFeign.pcafeDataSync(request);
        }else {
            //走uat环境
            voidResult =  vehicleTreeTaskTestFeign.pcafeDataSync(request);
        }
        log.info("同步数据结果为{}",voidResult);
        if (!voidResult.isOk()){
            log.info("同步数据失败");
            throw new ServerException("同步失败:"+voidResult.getMsg());
        }
        //更新功能树状态
        if (environment.equals("prod")){
            for (FunctionTreeSyncTaskInfoListVO infoListVO : infoList) {
                testPlatformFunctionTreeService.updateFunctionTreeState(Integer.valueOf(taskEntity.getTestRecordId()),infoListVO.getFunctionTag(),FunctionTreeDataStateEnum.ONLINE.getValue());
            }
        }
        //记录同步日志数据
        FunctionTreeTaskSyncJournalResponse data = voidResult.getData();
        FunctionTreeSyncTaskJournalEntity entity = FunctionTreeSyncTaskJournalConvert.INSTANCE.convert(data);
        entity.setEnvironment(environment);
        entity.setOperationId(operationId);
        entity.setSyncInfoJournal(JsonUtils.toJsonString(data));
        functionTreeSyncTaskJournalDao.insert(entity);
    }


    /**
     * 获取同步数据转换请求参数
     * @param taskEntity
     * @return
     */
    private Result<List<FunctionTreeDataSyncInfoRequest>> getSyncInfoRequests(FunctionTreeSyncTaskEntity taskEntity,List<FunctionTreeSyncTaskInfoListVO> infoList) {
        List<FunctionTreeDataSyncInfoRequest> infoRequests = infoList.stream().map(it -> {
            //查询功能树状态详情
            FunctionTreeStateEntity stateEntity = functionTreeStateDao.selectOne(Wrappers.<FunctionTreeStateEntity>lambdaQuery()
                    .eq(FunctionTreeStateEntity::getRecordId, taskEntity.getTestRecordId())
                    .eq(FunctionTreeStateEntity::getTaskDetail, it.getFunctionTag())
            );
            if (Objects.isNull(stateEntity)) {
                log.info("功能树状态详情不存在，创建默认，功能树tag：{}，serial：{}", it.getFunctionTag(), it.getTaskSerial());
//                return null;
                stateEntity = new FunctionTreeStateEntity();
                stateEntity.setFunctionEvaluate(FunctionTreeEvaluteStateEnum.UN.getValue());
                stateEntity.setDataState(FunctionTreeDataStateEnum.LACK_OF_DATA.getValue());
            }
            FunctionTreeEvaluteStateEnum stateEnum = FunctionTreeEvaluteStateEnum.paseEnum(stateEntity.getFunctionEvaluate());
            if (Objects.isNull(stateEnum)) {
                log.info("功能树状态详情状态错误，功能树tag：{}，serial：{}", it.getFunctionTag(), it.getTaskSerial());
                return null;
            }
            FunctionTreeDataSyncInfoRequest infoRequest = new FunctionTreeDataSyncInfoRequest();
            infoRequest.setFunctionTag(it.getFunctionTag());
            infoRequest.setTagNumber(it.getTargetFunctionTag());
            infoRequest.setFunctionEvaluate(stateEnum.getCode());
//            infoRequest.setSyncOption(it.getSyncOption());

            //根据用例id进行拆分
            String syncOption = it.getSyncOption();
            Map<Integer,String> caseOptionMap = new HashMap<>();
            //拆分出每一个用例
            if (StringUtils.isNotBlank(syncOption)) {
               //拆分出每一个用例
                String[] split = syncOption.split(";");
                for (String option : split) {
                    if (StringUtils.isBlank(option) || !option.contains("_")) {
                        continue;
                    }
                    String[] split1 = option.split("_");
                    if (split1.length>=2){
                        caseOptionMap.put(Integer.valueOf(split1[0]),split1[1]);
                    }
                }
            }
            //查询功能测试用例数据
            List<FunctionTreeDataSyncCaseInfoDto> infoDtos = it.getTestCaseId().stream().map(caseId -> {
                TestPlatformVehicleTestStateEntity testStateEntity = testPlatformVehicleTestStateDao.selectOne(Wrappers.<TestPlatformVehicleTestStateEntity>lambdaQuery()
                        .eq(TestPlatformVehicleTestStateEntity::getRecordId, taskEntity.getTestRecordId())
                        .eq(TestPlatformVehicleTestStateEntity::getTestcaseId, caseId)
                );
                if (Objects.isNull(testStateEntity)) {
                    log.info("未查询出用例id详情：id:{},tag:{}.serial：{}", caseId, it.getFunctionTag(), it.getTaskSerial());
                    return null;
                }
                FunctionTreeDataSyncCaseInfoDto infoDto = new FunctionTreeDataSyncCaseInfoDto();
                infoDto.setId(caseId);
                infoDto.setTestStateId(testStateEntity.getId());
                infoDto.setIsShow(FunctionTreeTestCaseRateStateEnum.Show.equals(testStateEntity.getTestCaseRate()));
                infoDto.setFunctionEvaluation(String.valueOf(FunctionTreeResultMeterialEnum.paseEnumDefaultValue(testStateEntity.getMaterialState()).getCode()));
                infoDto.setTestCaseRate(testStateEntity.getTestCaseRate());
                infoDto.setSyncOption(caseOptionMap.get(caseId));
                return infoDto;
            }).filter(Objects::nonNull).peek(infoDto -> {
                if (FunctionTreeTestCaseRateStateEnum.Poor.equals(infoDto.getFunctionEvaluation())){
                    infoDto.setCaseFileMaterial(new ArrayList<>());
                }else {
                    //查询用例文件素材
                    List<TestCaseMaterialEntity> testCaseMaterialEntities = testCaseMaterialDao.selectList(Wrappers.<TestCaseMaterialEntity>lambdaQuery()
                            .eq(TestCaseMaterialEntity::getTestStateId, infoDto.getTestStateId())
                            .orderByDesc(TestCaseMaterialEntity::getCreateTime)
                    );
                    List<TestCaseMaterialEntity> entities = new ArrayList<>();
                    if (!testCaseMaterialEntities.isEmpty()) {
                        //筛选出素材
                        entities = testCaseMaterialEntities.stream().filter(str -> Objects.nonNull(str.getIsShow()) && str.getIsShow()).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(entities)) {
                            TestCaseMaterialEntity last = testCaseMaterialEntities.getLast();
                            if (Objects.nonNull(last)) {
                                entities.add(last);
                            }
                        }
                    }
                    List<FunctionTreeDataSyncCaseInfoDto.CaseFileMaterial> materialVos = TestCaseMaterialConvert.INSTANCE.converResList(entities);
                    infoDto.setCaseFileMaterial(materialVos);
                }
            }).toList();

            infoRequest.setFunctionCaseData(infoDtos);
            return infoRequest;
        }).filter(Objects::nonNull).toList();
        return Result.ok(infoRequests);
    }

}