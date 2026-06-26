package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaivehicledata.entity.PcafeRelevancyFunctionThreeTagEntity;

import java.util.Map;

/**
 * 
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-05-26 09:43:10
 */
public interface PcafeRelevancyFunctionThreeTagService extends IService<PcafeRelevancyFunctionThreeTagEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

