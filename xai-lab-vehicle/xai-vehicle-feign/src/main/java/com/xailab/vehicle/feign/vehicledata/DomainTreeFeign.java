package com.xailab.vehicle.feign.vehicledata;


import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.vo.FunctionDomainVideoVo;
import com.xailab.vehicle.feign.vo.FunctionalVideoListResponse;
import com.xailab.vehicle.feign.vo.FunctionalVideoVoF;
import com.xailab.vehicle.feign.vo.OneIDRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/vehicle/domaintree",contextId = "functionDomainTreeFeign")

public interface DomainTreeFeign {
    @PostMapping("/editDomainVideo")
    public FunctionalVideoListResponse editDomainVideo(@RequestBody OneIDRequest vo);
    @PostMapping("/queryVideoByFunctionalVideoVo")
    FunctionDomainVideoVo queryVideoByFunctionalVideoVo(@RequestBody FunctionalVideoVoF functionalVideoVo);

    @PostMapping("/updateFunctionDomainVideoInfo")
    Boolean updateFunctionDomainVideoInfo(@RequestBody FunctionDomainVideoVo functionDomainVideoVo);
}
