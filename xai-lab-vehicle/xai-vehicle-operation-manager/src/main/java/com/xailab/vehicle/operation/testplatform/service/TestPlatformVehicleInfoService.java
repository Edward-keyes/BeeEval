package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleInfoVO;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleInfoQuery;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleInfoEntity;

import java.util.List;

/**
 * 车辆信息表
 *
 * @author mm 
 * @since 1.0.0 2025-04-17
 */
public interface TestPlatformVehicleInfoService extends BaseService<TestPlatformVehicleInfoEntity> {

    PageResult<TestPlatformVehicleInfoVO> page(TestPlatformVehicleInfoQuery query);

    List<TestPlatformVehicleInfoVO> getVehicleList();

    void save(TestPlatformVehicleInfoVO vo);

    void update(TestPlatformVehicleInfoVO vo);

    void delete(List<Long> idList);
}