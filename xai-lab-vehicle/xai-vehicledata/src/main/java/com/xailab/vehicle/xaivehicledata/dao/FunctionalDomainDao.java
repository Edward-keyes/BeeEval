package com.xailab.vehicle.xaivehicledata.dao;

import com.xailab.vehicle.feign.vo.DomainRelevancyVo;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.FunctionalDomainEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionalDomainResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface FunctionalDomainDao extends BaseMapper<FunctionalDomainEntity> {

    @Select("select id,functional_domain_name from vehicle_functional_domain")
    List<FunctionalDomainEntity> queryIdAndDomainName();



    List<FunctionalDomainAvgScoreVo> queryDomainAveScore(@Param("domainTagName") String domainTagName,@Param("language") String language);



    DomainTagAvgScoreVo queryDomainTagAvg(@Param("domainTagName") String domainTagName);

    List<FunctionalDomainResponse> queryDomainTree(@Param("language") String language);

    List<GeneralAbilityVo> queryGeneralAbilityVos(@Param("vehicleId")List<String> vehicleId,@Param("language") String language);

    List<ActionAbilityVo> queryActionAbilityVos(@Param("vehicleId")List<String> vehicleId,@Param("language") String language);

    List<GeneralAbilityVo> queryGeneralAbilityAvgVos(@Param("vehicleId")List<String> vehicleId,@Param("language") String language);

    List<ActionAbilityVo> queryActionAbilityAvgVos(@Param("vehicleId")List<String> vehicleId,@Param("language") String language);

    List<FunctionDomainIndexVo> queryAllFunctionDomainData();

    List<DomainRelevancyVo> queryRelevancy();

    List<com.xailab.vehicle.feign.vo.FunctionDomainIndexVo> queryRelevancyIndex();
}
