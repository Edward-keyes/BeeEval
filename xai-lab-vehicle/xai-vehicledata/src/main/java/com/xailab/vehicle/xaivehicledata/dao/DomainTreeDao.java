package com.xailab.vehicle.xaivehicledata.dao;

import com.xailab.vehicle.xaivehicledata.entity.DomainTreeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 
 * 
 * 
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
@Mapper
public interface DomainTreeDao extends BaseMapper<DomainTreeEntity> {

    @Select("select id from vehicle_domain_tree where domain_index_id=#{indexId} and vehicle_base_id = #{vehicleId}")
    Long findIdByDomainIndexIdAndVehicleBaseId(@Param("indexId") Long indexId, @Param("vehicleId") Long vehicleId);
}
