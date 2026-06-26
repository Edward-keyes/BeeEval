package com.xailab.vehicle.operation.beeeval.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xailab.vehicle.feign.vehicledata.*;
import com.xailab.vehicle.feign.vo.FunctionTreeCaseFeignVo;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationVoRequest;
import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.framework.common.utils.JsonUtils;
import com.xailab.vehicle.operation.beeeval.entity.vo.PcafeRelevancyFunctionThreeTagVos;
import com.xailab.vehicle.framework.security.user.SecurityUser;
import com.xailab.vehicle.framework.security.user.UserDetail;
import com.xailab.vehicle.operation.beeeval.entity.response.FunctionTreeSynchronizationResponse;
import com.xailab.vehicle.operation.beeeval.entity.vo.PcafeUnrelevancyVo;
import com.xailab.vehicle.operation.beeeval.service.FunctionTreeSynchronizationService;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestCaseDao;
import com.xailab.vehicle.operation.testplatform.vo.PcafeRelevancyFunctionThreeTagVo;
import com.xailab.vehicle.operation.testplatform.vo.TestCaseContentVo;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformFunctionTreeVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@DS("test_platform")
@Slf4j
public class FunctionTreeSynchronizationServiceImpl implements FunctionTreeSynchronizationService {

    @Resource
    FunctionOneTagFeign functionOneTagFeign;

    @Resource
    private TestPlatformVehicleTestCaseDao testPlatformVehicleTestCaseDao;

    @Resource
    VehiclePcafeRelevancyFunctionThreeTagFeign vehiclePcafeRelevancyFunctionThreeTagFeign;

    @Resource
    FunctionThreeTagFeign functionThreeTagFeign;

    @Resource
    VehicleTreeCaseFeign functionTreeCaseFeign;

    @Override
    public FunctionTreeSynchronizationResponse functionTreeSynchronizationQuery() {

        FunctionTreeSynchronizationResponse functionTreeSynchronizationResponse = new FunctionTreeSynchronizationResponse();

        functionTreeSynchronizationResponse.setOneAndTwoTagData(functionOneTagFeign.getAllOneAndTwoTag());

        List<PcafeRelevancyFunctionThreeTagEntity> pcafeRelevancyFunctionThreeTagEntities = vehiclePcafeRelevancyFunctionThreeTagFeign.queryList();

        List<TestPlatformFunctionTreeVo> records = testPlatformVehicleTestCaseDao.selectFunctionTreeTagList(6);

        functionTreeSynchronizationResponse.setRelevancyData(convert(pcafeRelevancyFunctionThreeTagEntities));

        Map<String, String> collect = pcafeRelevancyFunctionThreeTagEntities
                .stream().collect(Collectors.
                        toMap(PcafeRelevancyFunctionThreeTagEntity::getPecafeThreeNumber,
                                PcafeRelevancyFunctionThreeTagEntity::getBeeevalThreeNumber));

        List<PcafeUnrelevancyVo> pcafeUnrelevancyVos = new ArrayList<>();

        for (TestPlatformFunctionTreeVo r:records){

            if (Objects.isNull(collect.get(r.getTaskDetail()))){

                PcafeUnrelevancyVo pcafeUnrelevancyVo = new PcafeUnrelevancyVo();

                pcafeUnrelevancyVo.setPecafeThreeNumber(r.getTaskDetail());

                pcafeUnrelevancyVos.add(pcafeUnrelevancyVo);

            }

        }

        functionTreeSynchronizationResponse.setPcafeUnrelevancyData(pcafeUnrelevancyVos);

        return functionTreeSynchronizationResponse;
    }

    public static List<PcafeRelevancyFunctionThreeTagVos> convert(List<PcafeRelevancyFunctionThreeTagEntity> entityList) {
        // 用于临时分组存储的Map，Key: id + "_" + beeevalThreeNumber
        Map<String, PcafeRelevancyFunctionThreeTagVos> map = new TreeMap<>();

        for (PcafeRelevancyFunctionThreeTagEntity entity : entityList) {
            // 构建唯一分组键 (使用id和beeevalThreeNumber)
            String key = entity.getId() + "_" + entity.getBeeevalThreeNumber();

            // 如果Map中不存在该Key，则新建VO对象并加入Map
            PcafeRelevancyFunctionThreeTagVos vo = map.computeIfAbsent(key, k -> {
                PcafeRelevancyFunctionThreeTagVos newVo = new PcafeRelevancyFunctionThreeTagVos();
                newVo.setId(entity.getId());
                newVo.setBeeevalThreeNumber(entity.getBeeevalThreeNumber());
                newVo.setPecafeThreeNumber(new ArrayList<>()); // 初始化空列表
                newVo.setBeeevalThreeName(String.join("-",entity.getBeeevalThreeNumber(),entity.getBeeevalThreeName()));
                return newVo;
            });

            // 将当前entity的pecafeThreeNumber添加到VO的List中
            vo.getPecafeThreeNumber().add(entity.getPecafeThreeNumber());
        }

        // 返回Map中所有Value构成的List
        return new ArrayList<>(map.values());
    }

    /**
     * 同步至beeeval数据库
     * @param functionTreeSynchronizationRequest
     * @return
     */
    @Override
    public Boolean functionTreeSynchronization(FunctionTreeSynchronizationVoRequest functionTreeSynchronizationRequest) {

        UserDetail user = SecurityUser.getUser();

        assert user != null;
        functionTreeSynchronizationRequest.setUpdater(user.getId());

        return functionThreeTagFeign.syncToBeeeval(functionTreeSynchronizationRequest);

    }

    @Override
    public void initCaseDataSynchronization() {

        Map<String, String> collect = vehiclePcafeRelevancyFunctionThreeTagFeign.queryList()
                .stream()
                .collect(Collectors
                        .toMap(PcafeRelevancyFunctionThreeTagEntity::getPecafeThreeNumber,
                                PcafeRelevancyFunctionThreeTagEntity::getBeeevalThreeNumber));

        List<TestCaseContentVo> result=testPlatformVehicleTestCaseDao.queryTestCaseContent();

        List<FunctionTreeCaseFeignVo> functionTreeCaseFeignVoList = new ArrayList<>();

        for (TestCaseContentVo testCaseContentVo : result) {

            String s = collect.get(testCaseContentVo.getTaskDetail());

            if (Objects.nonNull(s)) {

                FunctionTreeCaseFeignVo functionTreeCaseFeignVo = getFunctionTreeCaseFeignVo(testCaseContentVo, s);

                functionTreeCaseFeignVoList.add(functionTreeCaseFeignVo);

            }
        }

        String jsonString = JsonUtils.toJsonString(functionTreeCaseFeignVoList);

        System.out.println(jsonString);

        Boolean b = functionTreeCaseFeign.saveBatch(functionTreeCaseFeignVoList);

    }

    @NotNull
    private static FunctionTreeCaseFeignVo getFunctionTreeCaseFeignVo(TestCaseContentVo testCaseContentVo, String s) {

        FunctionTreeCaseFeignVo functionTreeCaseFeignVo = new FunctionTreeCaseFeignVo();

        functionTreeCaseFeignVo.setId(testCaseContentVo.getId());

        functionTreeCaseFeignVo.setCaseContent(testCaseContentVo.getTestcaseContent());

        functionTreeCaseFeignVo.setThreeTagId(s);

        functionTreeCaseFeignVo.setUpdateTime(new Date());

        functionTreeCaseFeignVo.setCreateTime(new Date());

        return functionTreeCaseFeignVo;
    }
}
