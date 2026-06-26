package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.DomainTestCaseEntity;
import com.xailab.vehicle.xaivehicledata.entity.response.DomainTestCaseResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DomainTestCaseDao extends BaseMapper<DomainTestCaseEntity> {
    DomainTestCaseEntity getByVehicleIdAndDomainIndexId(@Param("vehicleId") String vehicleId,@Param("domainIndexId") String domainIndexId);

    List<DomainTestCaseResponse> queryCaseByVehicleIdAndDomainIndexId(@Param("domainIndexId") String domainIndexId,@Param("vehicleIds") List<String> vehicleId,@Param("language") String language);
}
