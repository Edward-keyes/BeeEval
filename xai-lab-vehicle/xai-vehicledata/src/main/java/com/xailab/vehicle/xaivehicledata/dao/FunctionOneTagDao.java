package com.xailab.vehicle.xaivehicledata.dao;

import com.xailab.vehicle.xaivehicledata.entity.FunctionOneTagEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.request.FunctionTreeVideoRequest;
import com.xailab.vehicle.xaivehicledata.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 *
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@Mapper
public interface FunctionOneTagDao extends BaseMapper<FunctionOneTagEntity> {

    List<FunctionalDomainVo> queryAllOneTagCountThreeTag(@Param("language") String language);

    List<HighlightFunctionVo> queryAllHighlightFunction(@Param("language") String language);

    List<FunctionTagVo> queryAllFunctionTag(@Param("language") String language);

    FunctionTreeVideoVo queryVideoByThreeTagIdAndVehicleId(@Param("vehicleId") String vehicleId, @Param("threeTagId") String threeTagId,@Param("language") String language);

    List<FunctionTreeCompareVehicleVo> queryOtherVideoByThreeTagId(@Param("threeTagId") String threeTagId, @Param("vehicleId") String vehicleId,@Param("vehicleIds")List<String> vehicleIds,@Param("language") String language);

    List<FunctionTreeOneAndTwoTagVo> getAllOneAndTwoTag();
}
