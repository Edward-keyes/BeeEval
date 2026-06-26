package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.config.MultipartSupportConfig;
import com.xailab.vehicle.feign.vo.AddVehicleInfoRequest;
import com.xailab.vehicle.feign.vo.VehicleInfoOpResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/brand"
//        ,configuration = MultipartSupportConfig.class
)
public interface VehicleInfoFeign {

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
    );

    @PostMapping("/vehicleInfoList")
    public List<VehicleInfoOpResponse> vehicleInfoList();

    @PostMapping("/queryVehiclePicture")
    String queryVehiclePicture(@RequestParam("pictureName") String pictureName);

    @PostMapping("/updateVehicleInfo")
    Integer updateVehicleInfo(@RequestBody VehicleInfoOpResponse vehicleInfoOpResponse);
}
