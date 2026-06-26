package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.feign.pojo.response.QueryVehicleTestcaseResponse;
import com.xailab.vehicle.operation.testplatform.entity.TertiaryMetricWeightEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionDomainScoreResponse;
import com.xailab.vehicle.operation.testplatform.pojo.response.TertiaryMetricWeightResponse;
import com.xailab.vehicle.operation.testplatform.vo.BaseFunctionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-22 17:05:43
 */
@Mapper
public interface TertiaryMetricWeightDao extends BaseMapper<TertiaryMetricWeightEntity> {

    List<QueryVehicleTestcaseResponse> queryTestcaseContentByVehicleId(@Param("vehicleId") List<String> vehicleId);

    List<TertiaryMetricWeightResponse> queryTertiaryMetricWeight(@Param("vehicleId") List<String> vehicleId);

    List<FunctionDomainScoreResponse> queryFunctionDomainScore(@Param("vehicleId") List<String> vehicleId);

    List<BaseFunctionVo> queryBaseWeight(@Param("vehicleId") String vehicleId);
}
