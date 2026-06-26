package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.vo.FunctionTreeCaseEntity;
import com.xailab.vehicle.feign.vo.FunctionTreeCaseFeignVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/functiontreecase",contextId = "vehicleFunctionTreeCaseFeign",url = "localhost:8181")

public interface VehicleTreeCaseFeign {

    /**
     * 基于threeId查询用例列表
     * @param threeId
     * @return
     */
    @PostMapping("/queryList")
    public List<FunctionTreeCaseEntity> queryList(@RequestBody String threeId);

    @PostMapping("/saveBatch")
    public Boolean saveBatch(@RequestBody List<FunctionTreeCaseFeignVo> list);

}
