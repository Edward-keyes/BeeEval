package com.xailab.vehicle.feign.vehicledata;

import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.feign.vo.PcafeRelevancyThreeTagVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME,path = "/xai-vehicledata/xaivehicledata/pcaferelevancyfunctionthreetag",contextId = "pcafeRelevancyFunctionThreeTagFeign")
public interface VehiclePcafeRelevancyFunctionThreeTagFeign {

    /**
     * 查询pcafe与threetag基于function number关联 列表
     * @return
     */
    @PostMapping("/queryList")
    List<PcafeRelevancyFunctionThreeTagEntity> queryList();

}