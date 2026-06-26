package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaivehicledata.entity.BrandEntity;
import com.xailab.vehicle.xaivehicledata.entity.response.VehicleInfoOpResponse;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Long getBrandIdByName(String brand);

    List<VehicleInfoOpResponse> queryVehicleInfolist();

    String queryVehiclePicture(String pictureName);

    Integer updateVehicleInfo(VehicleInfoOpResponse vehicleInfoOpResponse);
}

