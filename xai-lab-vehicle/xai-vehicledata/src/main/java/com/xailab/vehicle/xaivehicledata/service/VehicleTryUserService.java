package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleTryUserEntity;

import java.util.List;

public interface VehicleTryUserService extends IService<VehicleTryUserEntity> {
    /**
     * 保存试用记录
     */
    Result saveTryUser(VehicleTryUserEntity tryUser);

    /**
     * 获取试用记录列表
     */
    Result getTryUserList();

    /**
     * 根据用户ID获取试用记录列表
     */
    List<VehicleTryUserEntity> getTryUserListByUserId(Long userId);

    /**
     * 根据车辆ID获取试用记录列表
     */
    Result getTryUserListByVehicleId(Long vehicleId);

    /**
     * 根据用户ID和车辆ID获取试用记录
     */
    Result getTryUserByUserIdAndVehicleId(Long userId, Long vehicleId);
}