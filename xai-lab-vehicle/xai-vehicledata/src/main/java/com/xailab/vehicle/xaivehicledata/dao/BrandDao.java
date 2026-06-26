package com.xailab.vehicle.xaivehicledata.dao;

import com.xailab.vehicle.xaivehicledata.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.response.VehicleInfoOpResponse;
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
public interface BrandDao extends BaseMapper<BrandEntity> {

    @Select("SELECT id FROM vehicle_brand WHERE brand = #{name}")
    Long getBrandIdByName(@Param("name") String brand);

    List<VehicleInfoOpResponse> queryVehicleInfolist();
}
