package com.xailab.vehicle.xaivehicledata.entity.response;

import com.xailab.vehicle.xaivehicledata.entity.ThreeTagList;
import lombok.Data;

import java.util.List;

@Data
public class PenetrationRateResponse {

    /**
     * 功能分析渗透率结果列表
     */
    private List<ThreeTagList> penetrationRateList;

    /**
     * 车辆总数
     */
    private Integer VehicleCount;

}
