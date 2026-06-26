package com.xailab.vehicle.xaivehicledata.dao;

import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface BaseInfoDao extends BaseMapper<BaseInfoEntity> {

    List<VehicleInfoVo> queryAllVehicle(@Param("language") String language);

    List<VehicleDetailInfoVo> queryAllVehicleDetailInfo(@Param("language") String language);

    VehicleDetailInfoVo queryVehicleInfoByVehicleId(@Param("id") String id,@Param("language") String language);

    @Select("select id from vehicle_base_info where brand_id=#{brandId} and vehicle_model=#{vehicleModel}")
    Long findByBrandIdAndVehicleModel(@Param("brandId") Long brandId, @Param("vehicleModel") String vehicleModel);

    List<VehicleInfoVo> queryAllVehicleByIds(@Param("ids") List<Long> ids);


    List<VehicleInfoContScoreVo> queryVehicleInfoAndCountScore(@Param("brandInfoIds") List<Long> brandInfoIds, @Param("functionDomainId") String functionDomainId, @Param("domainTagName") String domainTagName,@Param("language") String language);

    List<VehicleInfoContScoreVo> queryVehicleInfoAndCountScoreByTag(@Param("brandInfoIds") List<Long> brandInfoIds, @Param("domainTagName") String domainTagName);

    List<VehicleInfoContScoreVo> queryVehicleInfoAndCountScoreByIndex(@Param("brandInfoIds") List<Long> brandInfoIds, @Param("domainIndexId") String domainIndexId,@Param("language")String language);

    List<BrandVehicleSystemVo> queryBrandVehicleVersion();

    List<BrandVehicleSystemVo> queryBrandVehicleEnVersion();

    List<VehicleConutThreeTagVo> queryAllVehicleConutThreeTag(@Param("language") String language);

}
