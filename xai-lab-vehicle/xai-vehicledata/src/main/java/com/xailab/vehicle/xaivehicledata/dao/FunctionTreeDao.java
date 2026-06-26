package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.FunctionTreeEntity;
import com.xailab.vehicle.xaivehicledata.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 
 * 
 *
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@Mapper
public interface FunctionTreeDao extends BaseMapper<FunctionTreeEntity> {

    List<PenetrationRateVO> queryPenetrationRateByFunctionTreeId(@Param("ids") List<String> ids,@Param("vehicleIds") List<String> vehicleIds,@Param("language") String language);

    List<FirstLevelTagRatioVo> queryFirstLevelTagRatioByVehicleIds(@Param("ids") List<String> ids,@Param("vehicleIds") List<String> vehicleIds,@Param("language") String language);

    List<VehicleFunctionGradeVo>
    queryVehicleFunctionGradeByFunctionTreeIdsAndVehicleIds
            (@Param("oneTagIds") List<String> oneTagIds
                    ,@Param("vehicleIds") List<String> vehicleIds
                    ,@Param("language") String language);

    FileUrlVo queryVideoOrPictureByThreeTagIdAndVehicleId(@Param("threeTagId")String threeTagId, @Param("vehicleId") String vehicleId,@Param("language") String language);

    List<FunctionRichnessRatioVo> queryFunctionRichnessRatioByVehicleId(@Param("id") String id,@Param("language") String language);

    List<PerceptionAbilityVo> queryPerceptionAbilityByVehicleId(@Param("id") List<String> id,@Param("language") String language);

    @Select("select * from vehicle_function_tree where function_three_tag_id=#{threeTagId} and vehicle_id=#{vehicleId}")
    FunctionTreeEntity queryOneByThreeTagIdAndVehicleId(@Param("threeTagId")Long threeTagId, @Param("vehicleId") Long vehicleId);
}
