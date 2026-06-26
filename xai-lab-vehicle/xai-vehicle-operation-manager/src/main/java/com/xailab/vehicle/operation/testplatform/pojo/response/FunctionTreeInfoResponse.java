package com.xailab.vehicle.operation.testplatform.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeInfoResponse
 * @Description:
 * @author: liulin
 * @date: 2025/5/18 23:07
 */
@Data
public class FunctionTreeInfoResponse implements Serializable {
    @Schema(description = "状态id")
    private Long id;

    /**
     * 测试任务id
     */
    private Integer recordId;

    @Schema(description = "车辆id")
    private Integer vehicleId;

    @Schema(description = "车型名称")
    private String vehicleName;

    @Schema(description = "车机软件版本")
    private String infotainmentSoftwareVersion;

    /**
     * 功能评价状态 na/modest/avg/good
     */
    private String functionEvaluate;

    /**
     * 数据状态 0/1/2/3 缺数据/可同步/待审核/已上线
     */
    private Integer dataState;

}
