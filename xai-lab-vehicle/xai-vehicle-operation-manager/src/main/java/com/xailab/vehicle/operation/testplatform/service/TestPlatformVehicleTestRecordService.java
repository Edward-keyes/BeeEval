package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformTestRecordVehicleVO;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleTestRecordVO;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestRecordQuery;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestRecordEntity;

import java.util.List;

/**
 * test_record
 *
 * @author mm 
 * @since 1.0.0 2025-04-17
 */
public interface TestPlatformVehicleTestRecordService extends BaseService<TestPlatformVehicleTestRecordEntity> {

    PageResult<TestPlatformVehicleTestRecordVO> page(TestPlatformVehicleTestRecordQuery query);

    void save(TestPlatformVehicleTestRecordVO vo);

    void update(TestPlatformVehicleTestRecordVO vo);

    void delete(List<Long> idList);

    List<TestPlatformTestRecordVehicleVO> findAll();
}