package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.vo.DomainRelevancyVo;
import com.xailab.vehicle.feign.vo.FunctionDomainIndexVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/vehicle/functionaldomain",contextId = "functionDomainFeign",url = "localhost:8181")

public interface FunctionalDomainFeign {

    @PostMapping("/queryRelevancy")
    public List<DomainRelevancyVo> queryRelevancy();

    @PostMapping("/queryRelevancyIndex")
    public List<FunctionDomainIndexVo> queryRelevancyIndex();
}
