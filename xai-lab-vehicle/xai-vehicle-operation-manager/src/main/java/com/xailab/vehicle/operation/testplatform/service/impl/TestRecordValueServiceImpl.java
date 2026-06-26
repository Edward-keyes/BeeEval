package com.xailab.vehicle.operation.testplatform.service.impl;

import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.dao.TestRecordValueDao;
import com.xailab.vehicle.operation.testplatform.entity.TestRecordValueEntity;
import com.xailab.vehicle.operation.testplatform.service.TestRecordValueService;
import org.springframework.stereotype.Service;

@Service
public class TestRecordValueServiceImpl extends BaseServiceImpl<TestRecordValueDao, TestRecordValueEntity> implements TestRecordValueService {
}
