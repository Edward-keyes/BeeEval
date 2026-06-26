package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserMonitorEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserMonitorService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicleusermonitor")
public class VehicleUserMonitorController {

    @Resource
    private VehicleUserMonitorService vehicleUserMonitorService;

    /**
     * 保存用户监控记录
     */
    @PostMapping("/save")
    public Result saveMonitor(@RequestBody VehicleUserMonitorEntity monitor) {
        return vehicleUserMonitorService.saveMonitor(monitor);
    }

    /**
     * 获取用户监控记录列表
     */
    @GetMapping("/list")
    public Result getMonitorList() {
        return vehicleUserMonitorService.getMonitorList();
    }

    /**
     * 根据用户ID获取监控记录列表
     */
    @GetMapping("/list/user/{userId}")
    public Result getMonitorListByUserId(@PathVariable("userId") Long userId) {
        return vehicleUserMonitorService.getMonitorListByUserId(userId);
    }

    /**
     * 根据事件类型获取监控记录列表
     */
    @GetMapping("/list/event/{eventType}")
    public Result getMonitorListByEventType(@PathVariable("eventType") String eventType) {
        return vehicleUserMonitorService.getMonitorListByEventType(eventType);
    }

    /**
     * 根据目标ID获取监控记录列表
     */
    @GetMapping("/list/target/{targetId}")
    public Result getMonitorListByTargetId(@PathVariable("targetId") String targetId) {
        return vehicleUserMonitorService.getMonitorListByTargetId(targetId);
    }
}