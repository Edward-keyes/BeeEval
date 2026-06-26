package com.xailab.vehicle.operation.testplatform.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* 功能树状态表
*
* @author mumu 
* @since 1.0.0 2025-05-18
*/
@Data
@Schema(description = "功能树状态表")
public class FunctionTreeStateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "功能树状态id")
	private Long id;

	@Schema(description = "功能域id(对应plan_detail表)")
	private Integer functionDomainId;

	@Schema(description = "测试任务id")
	private Integer recordId;

	@Schema(description = "场景任务")
	private String scenarioTask;

	@Schema(description = "任务细分")
	private String taskDetail;

	@Schema(description = "功能评价状态 na/modest/avg/good")
	private String functionEvaluate;

	@Schema(description = "数据状态 0/1/2 缺数据/可同步/待审核")
	private Integer dataState;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "修改时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;

	@Schema(description = "是否删除 0/1 否/是")
	private Integer deleted;


}