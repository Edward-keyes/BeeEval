package com.xailab.vehicle.xaivehicledata.dao;

import com.xailab.vehicle.feign.pojo.treem.FunctionTreeCaseMaterialResponse;
import com.xailab.vehicle.xaivehicledata.entity.FunctionCaseMaterialEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 功能用例素材表
 * 
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-08 01:22:23
 */
@Mapper
public interface FunctionCaseMaterialDao extends BaseMapper<FunctionCaseMaterialEntity> {

    @Select("select material_url from function_case_material where function_tree_case_id=#{caseId} and vehicle_id=#{vehicleId}")
    List<String> selectUrlByCaseId(@Param("caseId") Long caseId, @Param("vehicleId") Long vehicleId);


    @Select("select * from function_case_material where function_tree_case_id= #{caseId} and vehicle_id= #{vehicleId}")
    List<FunctionTreeCaseMaterialResponse> queryFunctionTreeCaseMaterial(@Param("caseId") Long caseId, @Param("vehicleId") Long vehicleId);

}
