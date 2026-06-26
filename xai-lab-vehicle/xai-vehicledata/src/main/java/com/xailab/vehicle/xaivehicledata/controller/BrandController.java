package com.xailab.vehicle.xaivehicledata.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xailab.vehicle.feign.vo.AddVehicleInfoRequest;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.response.VehicleInfoOpResponse;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.xailab.vehicle.xaivehicledata.entity.BrandEntity;
import com.xailab.vehicle.xaivehicledata.service.BrandService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * 
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@RestController
@RequestMapping("ware/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private BaseInfoService baseInfoService;

    @PostMapping("/vehicleInfoList")
    public List<VehicleInfoOpResponse> vehicleInfoList(){

        return brandService.queryVehicleInfolist();
    }

    @PostMapping("/queryVehiclePicture")
    String queryVehiclePicture(@RequestParam("pictureName") String pictureName){
        return brandService.queryVehiclePicture(pictureName);
    }

    @PostMapping("/updateVehicleInfo")
    Integer updateVehicleInfo(@RequestBody VehicleInfoOpResponse vehicleInfoOpResponse){
        return brandService.updateVehicleInfo(vehicleInfoOpResponse);
    }

    /**
     * 新增车辆信息
     * @param
     * @return
     */
    @RequestMapping(value = "/addVehicleInfo",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Boolean addVehicleInfo(
            @RequestParam("brand") String brand,
            @RequestParam("brand_en") String brand_en,
            @RequestParam("vehicleModel") String vehicleModel,
            @RequestParam("vehicleVersion") String vehicleVersion,
            @RequestParam("vehicleVersionEn") String vehicleVersionEn,
            @RequestParam("timeToMarket") Date timeToMarket,
            @RequestParam("testDate") Date testDate,
            @RequestParam("vehicleSystemVersion") String vehicleSystemVersion,
            @RequestParam("modelName") String modelName,
            @RequestParam("energyType") String energyType,
            @RequestParam("energyTypeEn") String energyTypeEn,
            @RequestParam("enduranceMileage") String enduranceMileage,
            @RequestParam("createDate") Date createDate,
            @RequestParam("updateDate") Date updateDate,
            @RequestParam("status") Integer status,
            @RequestParam("isDelete") Integer isDelete,
            @RequestParam("chipInformation") String chipInformation,
            @RequestParam("chipInformationEn") String chipInformationEn,
            @RequestPart("image") MultipartFile image
    ){
        AddVehicleInfoRequest vehicleInfo = new AddVehicleInfoRequest();
        vehicleInfo.setBrand(brand);
        vehicleInfo.setBrand_en(brand_en);
        vehicleInfo.setVehicleModel(vehicleModel);
        vehicleInfo.setVehicleVersion(vehicleVersion);
        vehicleInfo.setVehicleVersionEn(vehicleVersionEn);
        vehicleInfo.setTimeToMarket(timeToMarket);
        vehicleInfo.setTestDate(testDate);
        vehicleInfo.setVehicleSystemVersion(vehicleSystemVersion);
        vehicleInfo.setModelName(modelName);
        vehicleInfo.setEnergyType(energyType);
        vehicleInfo.setEnergyTypeEn(energyTypeEn);
        vehicleInfo.setEnduranceMileage(enduranceMileage);
        vehicleInfo.setCreateDate(createDate);
        vehicleInfo.setUpdateDate(updateDate);
        vehicleInfo.setStatus(status);
        vehicleInfo.setIsDelete(isDelete);
        vehicleInfo.setChipInformation(chipInformation);
        vehicleInfo.setChipInformationEn(chipInformationEn);
        Boolean b=baseInfoService.addVehicleInfo(vehicleInfo,image);

        return b;
    }

}
