package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.vo.FunctionTreeCaseFeignVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/functiontreecase",contextId = "functionTreeCaseFeign")

public interface FunctionTreeCaseFeign {

    @PostMapping("/saveBatch")
    public Boolean saveBatch(@RequestBody List<FunctionTreeCaseFeignVo> list);

}
