package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/aliyunoss",contextId = "aLiYunOssFeign")
public interface ALiYunOSSFeign {

    @PostMapping("/uploadPhoto")
    public String uploadPhoto(MultipartFile file);

}
