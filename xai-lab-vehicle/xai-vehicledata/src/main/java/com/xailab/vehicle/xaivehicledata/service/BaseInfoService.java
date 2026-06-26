package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleDetailInfoVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleInfoVo;
import org.springframework.web.multipart.MultipartFile;
import com.xailab.vehicle.feign.vo.AddVehicleInfoRequest;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
public interface BaseInfoService extends IService<BaseInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Long getVehicleIdByBMV(String brand, String model, String version);

    List<VehicleInfoVo> queryAllVehicle(String language);

    List<VehicleDetailInfoVo> queryAllVehicleDetailInfo(String language);

    VehicleDetailInfoVo queryVehicleInfoByVehicleId(String id,String language);

    Map<String, String> getBrandVehicleVersionMap();

    Map<String,String> getBrandVehicleVersionMap2();

    Map<String, String> getBrandVehicleVersionEnMap2();

    Boolean addVehicleInfo(AddVehicleInfoRequest addVehicleInfoRequest, MultipartFile image);
}

