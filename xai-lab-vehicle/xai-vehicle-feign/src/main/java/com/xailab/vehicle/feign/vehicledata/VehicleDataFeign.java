package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.vo.FunctionTreeOpResponse;
import com.xailab.vehicle.feign.vo.SortRequest;
import com.xailab.vehicle.feign.vo.VehicleInfoOpResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/functionthreetag",contextId = "vehicleDataFeign")
public interface VehicleDataFeign {

    @PostMapping("/getFunctionTagList")
    public List<FunctionTreeOpResponse> getFunctionTagList();

    @PostMapping("/sortThreeTag")
    public Integer sortThreeTag(SortRequest sortRequest);

}
