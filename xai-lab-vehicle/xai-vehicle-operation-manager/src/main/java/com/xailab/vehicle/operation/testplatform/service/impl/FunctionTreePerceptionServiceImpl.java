package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.dao.FunctionTreePerceptionDao;
import com.xailab.vehicle.operation.testplatform.dao.TestPerceptionTaskDao;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreePerceptionEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPerceptionTaskEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.PerceptionTreeResponse;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreePerceptionService;
import com.xailab.vehicle.operation.testplatform.vo.PerceptionTreeEditVo;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@DS("test_platform")
public class FunctionTreePerceptionServiceImpl extends BaseServiceImpl<FunctionTreePerceptionDao, FunctionTreePerceptionEntity> implements FunctionTreePerceptionService {

    @Resource
    private FunctionTreePerceptionDao functionTreePerceptionDao;

    @Resource
    private TestPerceptionTaskDao testPerceptionTaskDao;

    @Override
    public List<PerceptionTreeResponse> queryPerceptionTree(Integer recordId) {

        List<PerceptionTreeResponse> responses = new ArrayList<>();

        List<FunctionTreePerceptionEntity> functionTreePerceptionEntities =
                functionTreePerceptionDao.selectList(Wrappers
                        .<FunctionTreePerceptionEntity>lambdaQuery()
                        .eq(FunctionTreePerceptionEntity::getRecordId, recordId));

        List<TestPerceptionTaskEntity> testPerceptionTaskEntities = testPerceptionTaskDao.selectList(new QueryWrapper<>());

        Map<Integer, FunctionTreePerceptionEntity> collect =
                functionTreePerceptionEntities.stream()
                        .collect(Collectors
                                .toMap(FunctionTreePerceptionEntity::getPerceptionId, v -> v));

        if (!functionTreePerceptionEntities.isEmpty()){
            for (TestPerceptionTaskEntity testPerceptionTaskEntity : testPerceptionTaskEntities) {
                if (collect.containsKey(testPerceptionTaskEntity.getId())){
                    PerceptionTreeResponse ptr = new PerceptionTreeResponse();
                    ptr.setPerceptionName(testPerceptionTaskEntity.getPerceptionName());
                    ptr.setType(testPerceptionTaskEntity.getType());
                    ptr.setIsHave(collect.get(testPerceptionTaskEntity.getId()).getIsHave());
                    responses.add(ptr);
                }else{
                    PerceptionTreeResponse ptr = new PerceptionTreeResponse();
                    ptr.setPerceptionName(testPerceptionTaskEntity.getPerceptionName());
                    ptr.setType(testPerceptionTaskEntity.getType());
                    ptr.setIsHave(1);
                    responses.add(ptr);
                }
            }
        }else {
            for (TestPerceptionTaskEntity testPerceptionTaskEntity : testPerceptionTaskEntities) {
                PerceptionTreeResponse ptr = new PerceptionTreeResponse();
                ptr.setPerceptionName(testPerceptionTaskEntity.getPerceptionName());
                ptr.setType(testPerceptionTaskEntity.getType());
                ptr.setIsHave(1);
                responses.add(ptr);
            }
        }

        return responses;
    }

    @Override
    public Boolean editPerceptionTree(Integer recordId, List<PerceptionTreeEditVo> perceptionTreeEditVoList) {
        List<Boolean> booleans = new ArrayList<>();
        for (PerceptionTreeEditVo vo:perceptionTreeEditVoList){
            TestPerceptionTaskEntity testPerceptionTaskEntity = testPerceptionTaskDao.selectOne(Wrappers
                    .<TestPerceptionTaskEntity>lambdaQuery()
                    .eq(TestPerceptionTaskEntity::getPerceptionName, vo.getPerceptionName()));

            FunctionTreePerceptionEntity functionTreePerceptionEntity = new FunctionTreePerceptionEntity();
            functionTreePerceptionEntity.setRecordId(recordId);
            functionTreePerceptionEntity.setPerceptionId(testPerceptionTaskEntity.getId());
            functionTreePerceptionEntity.setIsHave(vo.getIsHave());
            functionTreePerceptionEntity.setCreateTime(new Date());
            booleans.add(functionTreePerceptionDao.insertOrUpdate(functionTreePerceptionEntity));
        }
        return booleans.contains(false);
    }
}
