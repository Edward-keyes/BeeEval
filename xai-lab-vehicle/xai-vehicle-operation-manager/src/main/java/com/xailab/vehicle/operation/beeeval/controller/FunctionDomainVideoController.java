package com.xailab.vehicle.operation.beeeval.controller;

import com.xailab.vehicle.feign.vehicledata.DomainTreeFeign;
import com.xailab.vehicle.feign.vo.FunctionDomainVideoVo;
import com.xailab.vehicle.feign.vo.FunctionalVideoListResponse;
import com.xailab.vehicle.feign.vo.FunctionalVideoVoF;
import com.xailab.vehicle.feign.vo.OneIDRequest;
import com.xailab.vehicle.framework.common.utils.Result;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/domainVideo")
@AllArgsConstructor
public class FunctionDomainVideoController {

    private final DomainTreeFeign domainTreeFeign;

    @GetMapping("/queryFunctionDomainVideoByVehicleId")
    @PreAuthorize("hasAnyAuthority('beeeval:function_domain_video:queryOldVideoInfoByVehicleId')")
    public Result<FunctionalVideoListResponse> queryFunctionDomainVideoByVehicleId(@RequestParam String vehicleId) {
        OneIDRequest id = new OneIDRequest();
        id.setId(vehicleId);
        FunctionalVideoListResponse functionalVideoListResponse = domainTreeFeign.editDomainVideo(id);

        return Result.ok(functionalVideoListResponse);
    }

    @PostMapping("/queryVideoByFunctionalVideoVo")
    @PreAuthorize("hasAnyAuthority('beeeval:function_domain_video:queryOldVideoUrl')")
    public Result<FunctionDomainVideoVo> queryVideoByFunctionalVideoVo(@RequestBody FunctionalVideoVoF functionalVideoVo) {

        FunctionDomainVideoVo functionDomainVideoVo = domainTreeFeign.queryVideoByFunctionalVideoVo(functionalVideoVo);

        return Result.ok(functionDomainVideoVo);
    }

    @PostMapping("/updateFunctionDomainVideoInfo")
    @PreAuthorize("hasAnyAuthority('beeeval:function_domain_video:updateVideoInfo')")
    public Result updateFunctionDomainVideoInfo(@RequestBody FunctionDomainVideoVo functionDomainVideoVo) {

        Boolean b = domainTreeFeign.updateFunctionDomainVideoInfo(functionDomainVideoVo);

        return Result.ok(b);
    }

}
