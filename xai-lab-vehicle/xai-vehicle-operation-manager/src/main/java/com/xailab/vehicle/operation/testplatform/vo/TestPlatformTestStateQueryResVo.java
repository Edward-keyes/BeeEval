package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: TestPlateformTestStateResVo
 * @Description:
 * @author: liulin
 * @date: 2025/4/17 23:14
 */
@Data
public class TestPlatformTestStateQueryResVo implements Serializable {

    /**
     * 测试 TestState id
     */
    private Integer id;

    /**
     * 测试状态 未测试：0；已测试：1
     */
    @Schema(description = "测试状态 未测试：0；已测试：1")
    private Integer testStatus;

    /**
     * 任务是否成功 初始：-1； 失败：0； 成功：1
     */
    @Schema(description = "任务是否成功 初始：-1； 失败：0； 成功：1")
    private Integer isSuccessful;

    /**
     * 打分
     */
    @Schema(description = "分数")
    private Integer score;

    /**
     * 错误类型
     */
    @Schema(description = "错误类型")
    private Integer errorType;

    /**
     * 错误详情
     */
    @Schema(description = "错误详情")
    private String errorDetail;

    /**
     *state视频名称
     */
    @Schema(description = "state视频名称")
    private String videoName;

    /**
     * 车辆id
     */
    private Integer vehicleId;
    /**
     * 车型名称
     */
    @Schema(description = "车型名称")
    private String vehicleName;

    /**
     * 车机软件版本
     */
    @Schema(description = "车机软件版本")
    private String infotainmentSoftwareVersion;



}
