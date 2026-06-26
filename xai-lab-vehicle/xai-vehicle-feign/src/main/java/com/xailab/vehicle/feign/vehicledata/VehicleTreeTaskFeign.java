package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncRequest;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeTaskSyncJournalResponse;
import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.feign.vo.SynchronizationThreeTagResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/functiontree",contextId = "vehicleTreeTaskFeign")
public interface VehicleTreeTaskFeign {

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