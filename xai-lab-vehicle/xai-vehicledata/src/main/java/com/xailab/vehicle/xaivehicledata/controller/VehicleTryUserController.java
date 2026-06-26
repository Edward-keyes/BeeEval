package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleTryUserEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleTryUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicletryuser")
public class VehicleTryUserController {

    @Resource
    private VehicleTryUserService vehicleTryUserService;

    /**
     * 保存试用记录
     */
    @PostMapping("/save")
    public Result saveTryUser(@RequestBody VehicleTryUserEntity tryUser) {
        return vehicleTryUserService.saveTryUser(tryUser);
    }

    /**
     * 获取试用记录列表
     */
    @GetMapping("/list")
    public Result getTryUserList() {
        return vehicleTryUserService.getTryUserList();
    }

    /**
     * 根据用户ID获取试用记录列表
     */
    @GetMapping("/list/user/{userId}")
    public Result getTryUserListByUserId(@PathVariable("userId") Long userId) {
        return Result.ok(vehicleTryUserService.getTryUserListByUserId(userId));
    }

    /**
     * 根据车辆ID获取试用记录列表
     */
    @GetMapping("/list/vehicle/{vehicleId}")
    public Result getTryUserListByVehicleId(@PathVariable("vehicleId") Long vehicleId) {
        return vehicleTryUserService.getTryUserListByVehicleId(vehicleId);
    }

    /**
     * 根据用户ID和车辆ID获取试用记录
     */
    @GetMapping("/get/{userId}/{vehicleId}")
    public Result getTryUserByUserIdAndVehicleId(
            @PathVariable("userId") Long userId,
            @PathVariable("vehicleId") Long vehicleId) {
        return vehicleTryUserService.getTryUserByUserIdAndVehicleId(userId, vehicleId);
    }
}