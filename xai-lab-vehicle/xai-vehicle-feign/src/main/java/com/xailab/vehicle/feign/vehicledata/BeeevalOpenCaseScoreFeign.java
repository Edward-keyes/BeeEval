package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.vo.VehicleIdAndOpenSourceVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/beeevalOpenCaseScore",contextId = "beeevalOpenCaseScoreFeign",url = "${xai-vehicledata.test-url:http://localhost:8181}")
public interface BeeevalOpenCaseScoreFeign {

    @PostMapping("/saveCaseScoreByVehicleId")
    public Boolean saveCaseScoreByVehicleId(VehicleIdAndOpenSourceVo vo);

}
