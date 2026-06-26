package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaivehicledata.entity.FunctionTwoTagEntity;

import java.util.Map;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
public interface FunctionTwoTagService extends IService<FunctionTwoTagEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

