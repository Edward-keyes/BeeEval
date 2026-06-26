package com.xailab.vehicle.feign.vehicleOperationManager;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.feign.vo.FunctionTreeSyncTaskVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME_manage,path = "/xai-vehicle-operation-manager/testplatform/sync_task",contextId = "syncTaskFeign")
public interface FunctionTreeSyncTaskFeign {

    @PostMapping("/saveAuditTask")
    public Result<Boolean> saveAuditTask(@RequestBody FunctionTreeSyncTaskVo request);

}
