package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.feign.vo.DomainRelevancyVo;
import com.xailab.vehicle.feign.vo.FunctionDomainIndexVo;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaivehicledata.entity.FunctionalDomainEntity;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionDomainIndexMapVo;


import java.util.List;
import java.util.Map;

/**
 * 
 *
 * 
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
public interface FunctionalDomainService extends IService<FunctionalDomainEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String, List<FunctionDomainIndexMapVo>> queryAllFunctionDomainData();

    List<DomainRelevancyVo> queryRelevancy();

    List<FunctionDomainIndexVo> queryRelevancyIndex();
}

