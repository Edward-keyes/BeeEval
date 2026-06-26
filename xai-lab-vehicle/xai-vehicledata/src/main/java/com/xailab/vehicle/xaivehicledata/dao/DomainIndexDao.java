package com.xailab.vehicle.xaivehicledata.dao;
import cn.hutool.log.Log;
import com.xailab.vehicle.xaivehicledata.entity.response.DomainIndexDetail;
import com.xailab.vehicle.xaivehicledata.entity.vo.*;
import org.apache.ibatis.annotations.Param;

import com.xailab.vehicle.xaivehicledata.entity.DomainIndexEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * 
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
@Mapper
public interface DomainIndexDao extends BaseMapper<DomainIndexEntity> {


    Long findIdByFunctionalDomainAndIndexName(@Param("functionalDomain") Long functionalDomain, @Param("indexName") String indexName);


    List<DomainIndexAvgScoreVo> findDomainAvgScore(@Param("functionalDomain") Long functionalDomain,@Param("language")String language);


    List<VehicleInfoDomainIndexScoreVo> findDomainVehicleInfoId(@Param("vehicleInfoIds") List<Long> vehicleInfoIds, @Param("domainIndexId") String domainIndexId,@Param("language")String language);

    List<DomainIndexAvgScoreVo> findDomainIndexAvgScoreByDomainTag(@Param("domainTagName") String domainTagName,@Param("language")String language);

    List<DomainNameIndexVo> getDomainIndexMap();

    List<IndexDetailVo> queryDomainIndexDetail(@Param("language")String language,@Param("domainId")String domainId);
}
