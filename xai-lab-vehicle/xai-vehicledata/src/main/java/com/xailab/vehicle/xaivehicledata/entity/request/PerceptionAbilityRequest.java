package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
public class PerceptionAbilityRequest {

    /**
     * 二级指标名称
     */
    private String perceptionAbilityName;

    /**
     * 三级指标列表
     */
    private List<PerceptionAbilitySmall> perceptionAbilitySmallList;

    @Data
    @Accessors(chain = true)
    public static class PerceptionAbilitySmall implements Serializable {

        /**
         * 三级指标名称
         */
        private String threeTagName;

        List<Vehicle> vehicleList;

    }

    @Data
    @Accessors(chain = true)
    public static class Vehicle implements Serializable {

        /**
         * 车辆ID
         */
        private String vehicleId;

        /**
         * 车辆名称
         */
        private String vehicleName;

        /**
         * 是否有该能力
         * 0：无
         * 2：有
         */
        private String isHave;

    }

}
