package com.xailab.vehicle.xaivehicledata.dao;

import com.xailab.vehicle.feign.pojo.treem.FunctionTreeCaseResponse;
import com.xailab.vehicle.xaivehicledata.entity.FunctionTreeCaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能树用例表
 * 
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-08 01:22:23
 */
@Mapper
public interface FunctionTreeCaseDao extends BaseMapper<FunctionTreeCaseEntity> {


    List<FunctionTreeCaseResponse> queryFunctionTreeCaseList(@Param("tagNumber") String tagNumber, @Param("vehicleId") Long vehicleId);
	
}
