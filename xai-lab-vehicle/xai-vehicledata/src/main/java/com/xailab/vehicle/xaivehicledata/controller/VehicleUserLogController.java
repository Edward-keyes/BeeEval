package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserLogEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicleuserlog")
public class VehicleUserLogController {

    @Resource
    private VehicleUserLogService vehicleUserLogService;

    /**
     * 保存用户操作日志
     */
    @PostMapping("/save")
    public Result saveLog(@RequestBody VehicleUserLogEntity log) {
        return vehicleUserLogService.saveLog(log);
    }

    /**
     * 获取用户操作日志列表
     */
    @GetMapping("/list")
    public Result getLogList() {
        return vehicleUserLogService.getLogList();
    }

    /**
     * 根据用户ID获取操作日志列表
     */
    @GetMapping("/list/{userId}")
    public Result getLogListByUserId(@PathVariable("userId") Long userId) {
        return vehicleUserLogService.getLogListByUserId(userId);
    }
}