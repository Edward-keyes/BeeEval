package com.xailab.vehicle.operation.testplatform.service.impl;

import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.dao.TestPerceptionTaskDao;
import com.xailab.vehicle.operation.testplatform.entity.TestPerceptionTaskEntity;
import com.xailab.vehicle.operation.testplatform.service.TestPerceptionTaskService;
import org.springframework.stereotype.Service;

@Service
public class TestPerceptionTaskServiceImpl extends BaseServiceImpl<TestPerceptionTaskDao,TestPerceptionTaskEntity> implements TestPerceptionTaskService {
}
