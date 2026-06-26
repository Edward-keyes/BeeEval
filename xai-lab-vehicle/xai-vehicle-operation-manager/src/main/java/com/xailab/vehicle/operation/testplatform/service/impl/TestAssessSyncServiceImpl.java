package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.dao.TestAssessSyncDao;
import com.xailab.vehicle.operation.testplatform.entity.TestAssessSyncEntity;
import com.xailab.vehicle.operation.testplatform.service.TestAssessSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DS("test_platform")
@RequiredArgsConstructor
public class TestAssessSyncServiceImpl extends BaseServiceImpl<TestAssessSyncDao,TestAssessSyncEntity> implements TestAssessSyncService {

//    private final

    @Override
    public void defaultSync() {

    }
}