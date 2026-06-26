package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehiclePlanDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* 方案细分表
*
* @author mumu
* @since 1.0.0 2025-04-16
*/
@Mapper
public interface TestPlatformVehiclePlanDetailDao extends BaseDao<TestPlatformVehiclePlanDetailEntity> {

    @Select("select id from plan_detail")
    List<Integer> selectIdList();

    @Update("update plan_detail set case_serial_id = case_serial_id + 1 where id = #{id}")
    int updateCaseSerialIdIdIncrease(@Param("id") Integer id);

    @Select("select case_serial_id from plan_detail where id = #{id}")
    Integer selectCaseSerialId(@Param("id") Integer id);

    /**
     * 根据ID查询功能域名称
     */
    @Select("select plan_detail_name from plan_detail where id = #{id}")
    String selectPlanDetailNameById(@Param("id") Integer id);
}