package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaivehicledata.entity.DomainIndexEntity;
import com.xailab.vehicle.xaivehicledata.entity.response.BigModelCapabilityAssessmentBase;
import com.xailab.vehicle.xaivehicledata.entity.response.DomainIndexDetail;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionalDomainRepresentation;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalDomainVehicleVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.IndexDetailVo;


import java.util.List;
import java.util.Map;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
public interface DomainIndexService extends IService<DomainIndexEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<BigModelCapabilityAssessmentBase> largeModelCapabilityAssessment(List<String> vehicleId,String language);

    List<FunctionalDomainVehicleVo> largeModelCapabilityAssessmentFunction(List<String> ids);

    Map<String, String> getDomainIndexMap();

    DomainIndexDetail queryDomainIndexDetail(String language);

    List<IndexDetailVo> queryIndexDetailVoList(String language,String domainId);
}

