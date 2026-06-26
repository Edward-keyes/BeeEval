package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 车辆信息表
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@Mapper
public interface TestPlatformVehicleInfoDao extends BaseDao<TestPlatformVehicleInfoEntity> {
	
}