package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.vo.FunctionTreeOneAndTwoTagResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/functiononetag",contextId = "functiononetagFeign",url = "localhost:8181")
public interface FunctionOneTagFeign {

    @PostMapping("/getAllOneAndTwoTag")
    public List<FunctionTreeOneAndTwoTagResponse> getAllOneAndTwoTag();

}
