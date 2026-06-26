package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeListResponse;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationRequest;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationVoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @ClassName: FunctionThreeTagFeign
 * @Description:
 * @author: liulin
 * @date: 2025/6/8 19:08
 */

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/ware/functionthreetag",contextId = "functionThreeTagFeign")
public interface FunctionThreeTagFeign {

    /**
     * 获取三级标签树
     */
    @PostMapping("/treeList")
    List<FunctionTreeListResponse> findFunctionTreeList();

    /**
     * 将三级标签与三级标签用例 数据同步至Beeeval
     */
    @PostMapping("/syncToBeeeval")
    Boolean syncToBeeeval(FunctionTreeSynchronizationVoRequest request);

}