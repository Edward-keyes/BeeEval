package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaivehicledata.entity.DomainTagEntity;


import java.util.Map;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
public interface DomainTagService extends IService<DomainTagEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

