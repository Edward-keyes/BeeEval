package com.xailab.vehicle.operation.testplatform.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* 功能树数据同步表
*
* @author mumu 
* @since 1.0.0 2025-06-02
*/
@Data
@Schema(description = "功能树数据同步表")
public class FunctionTreeSyncTaskVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键ID")
	private Long id;

	@Schema(description = "任务流水号，随机")
	private String taskSerial;

	@Schema(description = "任务名称")
	private String taskName;

	@Schema(description = "任务描述")
	private String description;

	@Schema(description = "测试任务id，原始车辆")
	private String testRecordId;

	@Schema(description = "车辆id，目标车辆，beeeval平台对应车辆id")
	private String vehicleId;

	@Schema(description = "同步规则：0-默认结构关联，1-功能id关联，2-自定义关联")
	private Integer syncRule;

	@Schema(description = "任务状态：0-审核中，1-同步成功")
	private Integer taskState;

	@Schema(description = "任务审核失败原因")
	private String taskMessage;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "修改时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long creator;

    /**
     * 创建者名称
     */
	private String creatorName;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;

	/**
	 * 修改者名称
	 */
	private String updaterName;


}