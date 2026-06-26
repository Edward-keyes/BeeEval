package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehiclePlanDetailVO;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehiclePlanDetailQuery;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehiclePlanDetailEntity;

import java.util.List;

/**
 * 方案细分表
 *
 * @author mumu
 * @since 1.0.0 2025-04-16
 */
public interface TestPlatformVehiclePlanDetailService extends BaseService<TestPlatformVehiclePlanDetailEntity> {

    PageResult<TestPlatformVehiclePlanDetailVO> page(TestPlatformVehiclePlanDetailQuery query);

    void save(TestPlatformVehiclePlanDetailVO vo);

    void update(TestPlatformVehiclePlanDetailVO vo);

    void delete(List<Long> idList);
}