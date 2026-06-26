package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserMonitorEntity;

public interface VehicleUserMonitorService extends IService<VehicleUserMonitorEntity> {
    /**
     * 保存用户监控记录
     */
    Result saveMonitor(VehicleUserMonitorEntity monitor);

    /**
     * 获取用户监控记录列表
     */
    Result getMonitorList();

    /**
     * 根据用户ID获取监控记录列表
     */
    Result getMonitorListByUserId(Long userId);

    /**
     * 根据事件类型获取监控记录列表
     */
    Result getMonitorListByEventType(String eventType);

    /**
     * 根据目标ID获取监控记录列表
     */
    Result getMonitorListByTargetId(String targetId);
}