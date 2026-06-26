package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestScenarioEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* 场景表
*
* @author mumu
* @since 1.0.0 2025-04-16
*/
@Mapper
public interface TestPlatformVehicleTestScenarioDao extends BaseDao<TestPlatformVehicleTestScenarioEntity> {

    @Select("select id from test_scenario")
    List<Integer> selectIdList();
}