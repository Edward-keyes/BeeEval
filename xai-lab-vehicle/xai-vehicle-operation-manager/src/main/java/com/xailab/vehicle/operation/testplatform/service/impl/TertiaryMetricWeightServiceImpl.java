package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.pojo.response.QueryVehicleTestcaseResponse;
import com.xailab.vehicle.feign.vo.VehicleIdBVIdRequest;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestRecordDao;
import com.xailab.vehicle.operation.testplatform.dao.TestRecordValueDao;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestRecordEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestRecordValueEntity;
import com.xailab.vehicle.operation.testplatform.pojo.request.EditBasicFunctionRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.TertiaryMetricWeightScoreResponse;
import com.xailab.vehicle.operation.testplatform.vo.BaseFunctionVo;
import com.xailab.vehicle.operation.testplatform.vo.BaseValueQueryVo;
import com.xailab.vehicle.operation.testplatform.vo.EditBasicFunctionVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xailab.vehicle.operation.testplatform.dao.TertiaryMetricWeightDao;
import com.xailab.vehicle.operation.testplatform.entity.TertiaryMetricWeightEntity;
import com.xailab.vehicle.operation.testplatform.service.TertiaryMetricWeightService;

@Service("tertiaryMetricWeightService")
@DS("test_platform")
public class TertiaryMetricWeightServiceImpl extends ServiceImpl<TertiaryMetricWeightDao, TertiaryMetricWeightEntity> implements TertiaryMetricWeightService {

    @Resource
    TertiaryMetricWeightDao tertiaryMetricWeightDao;

    @Resource
    TestPlatformVehicleTestRecordDao testRecordDao;

    @Resource
    TestRecordValueDao testRecordValueDao;

    @Resource
    TestPlatformVehicleTestRecordDao testPlatformVehicleTestRecordDao;

    @Override
    public List<QueryVehicleTestcaseResponse> queryTestcaseContentByVehicleId(List<String> vehicleId) {

        return tertiaryMetricWeightDao.queryTestcaseContentByVehicleId(vehicleId);

    }

    @Override
    public TertiaryMetricWeightScoreResponse queryTertiaryMetricWeight(List<String> vehicleId) {

        TertiaryMetricWeightScoreResponse tertiaryMetricWeightScoreResponse = new TertiaryMetricWeightScoreResponse();

        //功能域得分
        tertiaryMetricWeightScoreResponse.setTertiaryMetricWeightResponses(tertiaryMetricWeightDao.queryTertiaryMetricWeight(vehicleId));

        //功能域下各三级指标得分
        tertiaryMetricWeightScoreResponse.setFunctionDomainScoreResponses(tertiaryMetricWeightDao.queryFunctionDomainScore(vehicleId));

        return tertiaryMetricWeightScoreResponse;
    }

    @Override
    public List<BaseFunctionVo> queryTertiaryMetricBaseWeight(String vehicleId) {
        /**
         * 基础能力 认知能力：
         * (基础能力)"自然语言理解（NLU）准确率",
         * (基础能力)"信息提取能力",
         * (基础能力)"语言推理能力",
         * (基础能力)"跨语言理解能力",
         * (基础能力)"文化伦理",
         * (基础能力)"通识知识",
         * (基础能力)"安全性"
         *
         * 基础能力 行动能力：
         * (专项测试)跨域协作能力
         */
        List<BaseFunctionVo> baseList=tertiaryMetricWeightDao.queryBaseWeight(vehicleId);
        String vehicleName ="";
        for (BaseFunctionVo b:baseList){
            vehicleName = b.getVehicleName();
            if (b.getFunctionName().equals("跨域协作能力")){
                b.setType(2);
                b.setIndexName("行动能力");
                b.setUnit("分");
            }else {
                b.setType(1);
                b.setIndexName("认知能力");
                b.setUnit("分");
            }

        }

//        Integer recordId = testPlatformVehicleTestRecordDao.selectOne(Wrappers.<TestPlatformVehicleTestRecordEntity>lambdaQuery().eq(TestPlatformVehicleTestRecordEntity::getVehicleId, vehicleId)).getId();

        /**
         * 基础能力 行动能力：
         * 图像生成速度
         * 文本生成速度
         * 首字响应时长
         */
        List<TestRecordValueEntity> testRecordValueEntities = testRecordValueDao.selectList(Wrappers.<TestRecordValueEntity>lambdaQuery().eq(TestRecordValueEntity::getRecordId, vehicleId));
        Map<String, TestRecordValueEntity> collect = testRecordValueEntities.stream().collect(Collectors.toMap(TestRecordValueEntity::getBaseFunction, v -> v));
        baseList.add(getRecordValue(vehicleName,"图像生成速度","秒/张",collect));
        baseList.add(getRecordValue(vehicleName,"文本生成速度","词/秒",collect));
        baseList.add(getRecordValue(vehicleName,"首字响应时长","秒",collect));

        /**
         * 基础能力 行动能力
         * (专项测试)拒识准确率
         * (专项测试)免唤醒准确率
         * 任务完成率
         */
        BaseValueQueryVo rejectionAccuracyRate
                =testRecordDao.queryBaseValue("拒识准确率",vehicleId);
        BaseValueQueryVo accuracyOfWakeWordFreeRecognition
                =testRecordDao.queryBaseValue("免唤醒准确率",vehicleId);
        BaseValueQueryVo taskCompletionRate
                =testRecordDao.getTaskCompletionRate(vehicleId);
        baseList.add(getRecordValueRate(vehicleName,"拒识准确率",rejectionAccuracyRate));
        baseList.add(getRecordValueRate(vehicleName,"免唤醒准确率",accuracyOfWakeWordFreeRecognition));
        baseList.add(getRecordValueRate(vehicleName,"任务完成率",taskCompletionRate));

        return baseList;
    }

    public BaseFunctionVo getRecordValueRate(String vehicleName,String functionName,BaseValueQueryVo c){

        BaseFunctionVo b = new BaseFunctionVo();
        b.setVehicleName(vehicleName);
        b.setFunctionName(functionName);
        b.setIndexName("行动能力");
        b.setType(2);
        if (Objects.nonNull(c)&&Objects.nonNull(c.getScorePercentage())) {
            b.setValue(c.getScorePercentage());
        }
        b.setUnit("%");

        return b;
    }

    public BaseFunctionVo getRecordValue(String vehicleName,String functionName,String unit,Map<String, TestRecordValueEntity> collect){
        BaseFunctionVo b = new BaseFunctionVo();
        if (collect.containsKey(functionName)) {
            TestRecordValueEntity t = collect.get(functionName);
            b.setValue(t.getValue());
            b.setType(2);
            b.setFunctionName(t.getBaseFunction());
            b.setIndexName("行动能力");
            b.setVehicleName(vehicleName);
            b.setUnit(unit);
        }else {
            b.setType(2);
            b.setFunctionName(functionName);
            b.setIndexName("行动能力");
            b.setVehicleName(vehicleName);
            b.setUnit(unit);
        }
        return b;
    }

        @Override
    public Boolean syncRule(VehicleIdBVIdRequest vehicleIdRequest) {

        //功能域关联规则
//        List<FunctionDomainRuleEntity> functionDomainRuleEntities = functionDomainRuleMapper.selectByVehicleId(vehicleIdRequest.getVehicleId());

        /**
         * 拼接 基础能力与功能域数据
         * 行动能力
         * 1.查询基础能力 拒识准确率
         * 2.查询基础能力 免唤醒准确率
         * 3.查询基础能力 任务完成率
         * 4.查询基础能力 跨域协作能力
         * 5.首字响应时长（ms）
         * 6.文本生成速（tokens/s）
         * 7.图像生成速（ms）
        */
        //1.拒识准确率

        //2.免唤醒准确率

        //3.任务完成率

        /**
         * 认知能力
         * bee eval function domain id = 450679990120349699
         * 文化伦理
         * 通识知识
         * 安全性
         * 自然语言理解准确率
         * 信息提取能力
         * 语言推理能力
         * 跨语言理解能力
         */

        return null;
    }

    @Override
    public List<BaseFunctionVo> queryBasicFunctionScore(List<String> vehicleIds) {
        List<BaseFunctionVo> request = new ArrayList<>();
        for (String vehicleId : vehicleIds) {
            request.addAll(queryTertiaryMetricBaseWeight(vehicleId));
        }
        return request;
    }

    @Override
    public Boolean editBasicFunctionScore(EditBasicFunctionRequest editBasicFunctionRequests) {

        List<EditBasicFunctionVo> basicFunctionList = editBasicFunctionRequests.getBasicFunctionList();

        TestPlatformVehicleTestRecordEntity testPlatformVehicleTestRecordEntity = testPlatformVehicleTestRecordDao

                .selectOne(Wrappers.<TestPlatformVehicleTestRecordEntity>lambdaQuery()

                        .eq(TestPlatformVehicleTestRecordEntity::getVehicleId, editBasicFunctionRequests.getVehicleId()));

        if (Objects.nonNull(testPlatformVehicleTestRecordEntity.getId())) {

            for (EditBasicFunctionVo editBasicFunctionRequest : basicFunctionList) {

                TestRecordValueEntity testRecordValueEntity = testRecordValueDao.selectOne(Wrappers.<TestRecordValueEntity>lambdaQuery()

                        .eq(TestRecordValueEntity::getBaseFunction, editBasicFunctionRequest.getBaseFunction())

                        .eq(TestRecordValueEntity::getRecordId, testPlatformVehicleTestRecordEntity.getId()));

                if (Objects.nonNull(testRecordValueEntity)) {

                    testRecordValueEntity.setValue(editBasicFunctionRequest.getValue());

                    testRecordValueDao.updateById(testRecordValueEntity);

                }else{

                    TestRecordValueEntity testRecordValueEntity1 = new TestRecordValueEntity();

                    testRecordValueEntity1.setBaseFunction(editBasicFunctionRequest.getBaseFunction());

                    testRecordValueEntity1.setValue(editBasicFunctionRequest.getValue());

                    testRecordValueEntity1.setRecordId(testPlatformVehicleTestRecordEntity.getId());

                    testRecordValueDao.insert(testRecordValueEntity1);

                }
            }
            return true;

        }else {

            return false;
        }

    }
}