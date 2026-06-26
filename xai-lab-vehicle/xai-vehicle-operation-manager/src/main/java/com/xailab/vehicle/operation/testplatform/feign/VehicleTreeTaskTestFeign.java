package com.xailab.vehicle.operation.testplatform.feign;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncRequest;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeTaskSyncJournalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/functiontree",contextId = "vehicleTreeTaskTestFeign",url = "${xai-vehicledata.test-url:http://localhost:8181}")
public interface VehicleTreeTaskTestFeign {

    /**
     * pcafe功能树数据同步至beeeval
     * @param request
     * @return
     */
    @PostMapping("/pcafeDataSync")
    Result<FunctionTreeTaskSyncJournalResponse> pcafeDataSync(@RequestBody FunctionTreeDataSyncRequest request);


    @PostMapping("/taskSyncFallback")
    Result<Void> taskSyncFallback(FunctionTreeTaskSyncJournalResponse response);
}