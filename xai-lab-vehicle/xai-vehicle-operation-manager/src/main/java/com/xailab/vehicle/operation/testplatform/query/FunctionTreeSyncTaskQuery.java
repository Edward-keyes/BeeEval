package com.xailab.vehicle.operation.testplatform.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.xailab.vehicle.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 功能树数据同步表查询
*
* @author mumu 
* @since 1.0.0 2025-06-02
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "功能树数据同步表查询")
public class FunctionTreeSyncTaskQuery extends Query {

    @Schema(description = "任务流水号，随机")
    private String taskSerial;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "测试任务id，原始车辆")
    private String testRecordId;

    @Schema(description = "车辆id，目标车辆，beeeval平台对应车辆id")
    private String vehicleId;

    @Schema(description = "同步规则：0-默认结构关联，1-功能id关联，2-自定义关联")
    private Integer syncRule;

    @Schema(description = "任务状态：0-审核中，1-同步成功 2-审核失败")
    private Integer taskState;

    /**
     * 创建人 id
     */
    private Long creator;
}