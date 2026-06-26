package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestRecordEntity;
import com.xailab.vehicle.operation.testplatform.vo.BaseValueQueryVo;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformTestRecordVehicleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* test_record
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@Mapper
public interface TestPlatformVehicleTestRecordDao extends BaseDao<TestPlatformVehicleTestRecordEntity> {

    List<TestPlatformTestRecordVehicleVO> findRecordVehicleList();

    BaseValueQueryVo queryBaseValue(@Param("functionName") String functionName,@Param("vehicleId") String vehicleId);

    BaseValueQueryVo getTaskCompletionRate(@Param("vehicleId") String vehicleId);
}