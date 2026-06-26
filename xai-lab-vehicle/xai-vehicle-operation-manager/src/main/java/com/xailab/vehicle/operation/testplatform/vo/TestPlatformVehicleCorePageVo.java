package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆详情 评分和差值
 */
@Data
public class TestPlatformVehicleCorePageVo implements Serializable {
    /**
     * 车辆id
     */
    private String vehicleId;

    /**
     * 车辆名称
     */
    private String vehicleName;

    /**
     * 车辆版本
     */
    private String vehicleVersion;

    /**
     * 车辆分数
     */
    private Integer score;

    /**
     * state视频名称
     */
    @Schema(description = "state视频名称")
    private String stateVideoName;

}