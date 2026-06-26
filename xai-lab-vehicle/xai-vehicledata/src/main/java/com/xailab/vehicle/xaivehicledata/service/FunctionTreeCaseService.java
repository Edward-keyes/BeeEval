package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaivehicledata.entity.FunctionTreeCaseEntity;

import java.util.Map;

/**
 * 功能树用例表
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-08 01:22:23
 */
public interface FunctionTreeCaseService extends IService<FunctionTreeCaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

