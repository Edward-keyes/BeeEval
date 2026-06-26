package com.xailab.vehicle.operation.testplatform.pojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeSyncCreateRequest
 * @Description:
 * @author: liulin
 * @date: 2025/6/2 19:29
 */
@Data
public class FunctionTreeSyncCreateRequest implements Serializable {

    /**
     * 编辑时需要
     */
    private String taskSerial;

    @Schema(description = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    @Schema(description = "任务描述")
    private String description;

    @Schema(description = "测试任务id，原始车辆")
    @NotBlank(message = "测试任务不能为空")
    private String testRecordId;

    @Schema(description = "车辆id，目标车辆，beeeval平台对应车辆id")
    @NotBlank(message = "目标车辆不能为空")
    private String vehicleId;

    @Schema(description = "同步规则：0-默认结构关联，1-功能id关联，2-自定义关联")
    @NotNull(message = "同步规则 不能为空")
    @Min(value = 0,message = "同步规则错误")
    @Max(value = 2,message = "同步规则错误")
    private Integer syncRule;

    /**
     * 创建同步任务详情
     */
    @NotEmpty(message = "创建同步任务详情不能为空")
    private List<FunctionTreeSyncTaskInfoRequest> syncTaskInfos;

}
