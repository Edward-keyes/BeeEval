package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserLogEntity;

public interface VehicleUserLogService extends IService<VehicleUserLogEntity> {
    /**
     * 保存用户操作日志
     */
    Result saveLog(VehicleUserLogEntity log);

    /**
     * 获取用户操作日志列表
     */
    Result getLogList();

    /**
     * 根据用户ID获取操作日志列表
     */
    Result getLogListByUserId(Long userId);
}