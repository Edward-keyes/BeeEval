package com.xailab.vehicle.operation.beeeval.controller;

import com.xailab.vehicle.feign.vehicledata.VehicleInfoFeign;
import com.xailab.vehicle.feign.vo.AddVehicleInfoRequest;
import com.xailab.vehicle.feign.vo.VehicleInfoOpResponse;
import com.xailab.vehicle.framework.common.utils.Result;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/vehicleInfo")
@AllArgsConstructor
public class VehicleInfoController {

    @Resource
    private VehicleInfoFeign vehicleInfoFeign;

    @PostMapping("/vehicleInfoList")
    @PreAuthorize("hasAuthority('beeeval:vehicle_info:page') || hasAuthority('testplatform:sync_task:page')")
    public Result<List<VehicleInfoOpResponse>> vehicleInfoList(){

        return Result.ok(vehicleInfoFeign.vehicleInfoList());
    }

    @PostMapping("/queryVehiclePicture")
    @PreAuthorize("hasAuthority('beeeval:vehicle_info:picture')")
    public Result<String> queryVehiclePicture(@RequestParam("pictureName") String pictureName){
        return Result.ok(vehicleInfoFeign.queryVehiclePicture(pictureName));
    }

    @PostMapping("/updateVehicleInfo")
    @PreAuthorize("hasAuthority('beeeval:vehicle_info:updateVehicleInfo')")
    public Result<String> updateVehicleInfo(@RequestBody VehicleInfoOpResponse vehicleInfoOpResponse){

        Integer i = vehicleInfoFeign.updateVehicleInfo(vehicleInfoOpResponse);
        if (i == 1){
            return Result.ok("更新成功！");
        }else {
            return Result.error("更新失败，请重新修改！");
        }
    }

    @PostMapping(value = "/addVehicleInfo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('beeeval:vehicle_info:addVehicleInfo')")
    public Result<String> addVehicleInfo(@RequestPart("vehicleInfo") AddVehicleInfoRequest vehicleInfo,
                                  @RequestPart("image") MultipartFile image){

        Boolean b = vehicleInfoFeign.addVehicleInfo(
                vehicleInfo.getBrand(),
                vehicleInfo.getBrand_en(),
                vehicleInfo.getVehicleModel(),
                vehicleInfo.getVehicleVersion(),
                vehicleInfo.getVehicleVersionEn(),
                vehicleInfo.getTimeToMarket(),
                vehicleInfo.getTestDate(),
                vehicleInfo.getVehicleSystemVersion(),
                vehicleInfo.getModelName(),
                vehicleInfo.getEnergyType(),
                vehicleInfo.getEnergyTypeEn(),
                vehicleInfo.getEnduranceMileage(),
                vehicleInfo.getCreateDate(),
                vehicleInfo.getUpdateDate(),
                vehicleInfo.getStatus(),
                vehicleInfo.getIsDelete(),
                vehicleInfo.getChipInformation(),
                vehicleInfo.getChipInformationEn()
                , image
        );

        if (b) {
            return Result.ok("添加成功！");
        }else {
            return Result.error("添加失败，请重新添加！");
        }
    }

}
