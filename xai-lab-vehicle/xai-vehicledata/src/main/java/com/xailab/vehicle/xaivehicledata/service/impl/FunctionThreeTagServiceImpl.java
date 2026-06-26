package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeCaseInfoResponse;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeListResponse;
import com.xailab.vehicle.feign.vehicleOperationManager.FunctionTreeSyncTaskFeign;
import com.xailab.vehicle.feign.vo.FunctionTreeSyncTaskVo;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationRequest;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationVoRequest;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.dao.FunctionTreeCaseDao;
import com.xailab.vehicle.xaivehicledata.entity.FunctionTreeCaseEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.SortRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionTreeOpResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.SynchronizationThreeTagResponse;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.Query;

import com.xailab.vehicle.xaivehicledata.dao.FunctionThreeTagDao;
import com.xailab.vehicle.xaivehicledata.entity.FunctionThreeTagEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionThreeTagService;
import org.springframework.util.CollectionUtils;


@Service("functionThreeTagService")
public class FunctionThreeTagServiceImpl extends ServiceImpl<FunctionThreeTagDao, FunctionThreeTagEntity> implements FunctionThreeTagService {

    @Autowired
    private FunctionThreeTagDao functionThreeTagDao;

    @Resource
    private FunctionTreeCaseDao functionTreeCaseDao;

    @Resource
    private FunctionTreeSyncTaskFeign functionTreeSyncTaskFeign;

    private static final SnowflakeIdGenerator snowflakeIdGenerator
            = new SnowflakeIdGenerator(0, 0);

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FunctionThreeTagEntity> page = this.page(
                new Query<FunctionThreeTagEntity>().getPage(params),
                new QueryWrapper<FunctionThreeTagEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String, Long> getAllIdAndTagNumber() {

        List<FunctionThreeTagEntity> functionThreeTagEntities
                = functionThreeTagDao.selectList(new QueryWrapper<>());

        Map<String, Long> collect = functionThreeTagEntities.stream().collect(
                Collectors.toMap(
                        FunctionThreeTagEntity::getTagNumber,
                        FunctionThreeTagEntity::getId));

        return collect;

    }

    @Override
    public List<FunctionTreeOpResponse> getFunctionTagList() {

        return functionThreeTagDao.getFunctionTagList();
    }

    @Override
    public Integer sortThreeTag(SortRequest sortRequest) {
        List<FunctionThreeTagEntity> functionThreeTagList=functionThreeTagDao.querySortThreeTag(sortRequest.getOnlyTag());
        //从上面排到下面
        if (sortRequest.getNewSortValue()>sortRequest.getOldSortValue()){
            List<FunctionThreeTagEntity> functionThreeTagEntityStream = functionThreeTagList.stream().filter(functionThreeTagEntity ->
                    functionThreeTagEntity.getSort() > sortRequest.getOldSortValue()
                            && !functionThreeTagEntity.getTagNumber().equals(sortRequest.getOnlyTag())
                            && functionThreeTagEntity.getSort() <= sortRequest.getNewSortValue()).collect(Collectors.toList());
            functionThreeTagEntityStream.forEach(functionThreeTagEntity -> functionThreeTagEntity.setSort(functionThreeTagEntity.getSort() - 1));
            FunctionThreeTagEntity changeThreeTagEntity = functionThreeTagList.stream().filter(functionThreeTagEntity -> functionThreeTagEntity
                            .getTagNumber().equals(sortRequest.getOnlyTag())).toList()
                    .getFirst();
            changeThreeTagEntity.setSort(sortRequest.getNewSortValue());
            functionThreeTagEntityStream.add(changeThreeTagEntity);
            boolean b = updateBatchById(functionThreeTagList);
            if (b){
                return 200;
            }else {
                return 500;
            }
        }
        //新排序值小于旧排序值
        else if(sortRequest.getNewSortValue()<sortRequest.getOldSortValue()){
            List<FunctionThreeTagEntity> functionThreeTagEntityStream = functionThreeTagList.stream().filter(functionThreeTagEntity ->
                    functionThreeTagEntity.getSort() < sortRequest.getOldSortValue()
                            && !functionThreeTagEntity.getTagNumber().equals(sortRequest.getOnlyTag())
                            && functionThreeTagEntity.getSort() >= sortRequest.getNewSortValue()).collect(Collectors.toList());
            functionThreeTagEntityStream.forEach(functionThreeTagEntity -> functionThreeTagEntity.setSort(functionThreeTagEntity.getSort() + 1));
            FunctionThreeTagEntity changeThreeTagEntity = functionThreeTagList.stream().filter(functionThreeTagEntity -> functionThreeTagEntity
                            .getTagNumber().equals(sortRequest.getOnlyTag())).toList()
                    .getFirst();
            changeThreeTagEntity.setSort(sortRequest.getNewSortValue());
            functionThreeTagEntityStream.add(changeThreeTagEntity);
            boolean b = updateBatchById(functionThreeTagList);
            if (b){
                return 200;
            }else {
                return 500;
            }
        }

        return null;
    }

    @Override
    public List<SynchronizationThreeTagResponse> getThreeTagListSynchronization() {

        return functionThreeTagDao.getThreeTagListSynchronization();
    }

    /**
     * 查询功能数list
     * @return
     */
    @Override
    public List<FunctionTreeListResponse> findFunctionTreeList() {
        List<FunctionTreeListResponse> functionTreeList = functionThreeTagDao.findFunctionTreeList();

        // 获取测试用例
        List<FunctionTreeCaseEntity> functionTreeCaseEntities =
                functionTreeCaseDao.selectList(new QueryWrapper<>());

        if (!CollectionUtils.isEmpty(functionTreeCaseEntities)){
            Map<String, List<FunctionTreeCaseEntity>> collect = functionTreeCaseEntities.stream().collect(Collectors.groupingBy(FunctionTreeCaseEntity::getThreeTagId));

            functionTreeList.forEach(functionTreeListResponse -> {

                List<FunctionTreeCaseEntity> functionTreeCaseEntityList = collect.get(functionTreeListResponse.getTagNumber());

                List<FunctionTreeCaseInfoResponse> functionTreeCaseInfoResponseList = new ArrayList<>();

                if (!CollectionUtils.isEmpty(functionTreeCaseEntityList)){

                    for (FunctionTreeCaseEntity functionTreeCase:functionTreeCaseEntityList){

                        FunctionTreeCaseInfoResponse functionTCIR = new FunctionTreeCaseInfoResponse();

                        functionTCIR.setCaseContent(functionTreeCase.getCaseContent());

                        functionTCIR.setThreeTagId(functionTreeCase.getThreeTagId());

                        functionTCIR.setCaseContentEn(functionTreeCase.getCaseContentEn());

                        functionTCIR.setId(functionTreeCase.getId());

                        functionTreeCaseInfoResponseList.add(functionTCIR);

                    }

                    functionTreeListResponse.setFunctionTreeCase(functionTreeCaseInfoResponseList);

                }
            });
        }
        return functionTreeList;
    }

    @Override
    public Boolean syncToBeeeval(FunctionTreeSynchronizationVoRequest requestVoList) {

        List<FunctionTreeSynchronizationRequest> requestList =
                requestVoList.getFunctionTreeSynchronizationRequest();

        List<FunctionThreeTagEntity> list = new ArrayList<>();

        String taskSerial="FD"+ snowflakeIdGenerator.nextId();

        for (FunctionTreeSynchronizationRequest request:requestList) {

            FunctionThreeTagEntity functionThreeTagEntity = new FunctionThreeTagEntity();

            functionThreeTagEntity.setFunctionTwoTagId(request.getSecondTagId());

            functionThreeTagEntity.setTagName(request.getFunctionTagName().split("-")[1]);

            functionThreeTagEntity.setTagNameEn(request.getFunctionTagNameEn().split("-")[1]);

            functionThreeTagEntity.setTagNumber(request.getFunctionTagName().split("-")[0]);

            functionThreeTagEntity.setStatus(0);

            functionThreeTagEntity.setTaskSerial(taskSerial);

            list.add(functionThreeTagEntity);
        }

        //TODO 调用fign,存储同步任务
        FunctionTreeSyncTaskVo functionTreeSyncTaskVo = new FunctionTreeSyncTaskVo();

        functionTreeSyncTaskVo.setTaskSerial(taskSerial);

        functionTreeSyncTaskVo.setTaskName(requestVoList.getTaskName());

        functionTreeSyncTaskVo.setDescription(requestVoList.getDescription());

        functionTreeSyncTaskVo.setTaskState(0);

        functionTreeSyncTaskVo.setCreateTime(LocalDateTime.now());

        functionTreeSyncTaskVo.setUpdateTime(LocalDateTime.now());

        functionTreeSyncTaskVo.setDeleted(0);

        functionTreeSyncTaskVo.setUpdater(requestVoList.getUpdater());

        functionTreeSyncTaskFeign.saveAuditTask(functionTreeSyncTaskVo);

        return saveBatch(list);
    }

}