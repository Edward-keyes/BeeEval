package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* 功能树同步任务详情表
*
* @author mm 
* @since 1.0.0 2025-06-02
*/
@Data
@Schema(description = "功能树同步任务详情表")
public class FunctionTreeSyncTaskInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键ID")
	private Long id;

	@Schema(description = "关联同步任务流水号")
	private String taskSerial;

	@Schema(description = "功能id")
	private String functionTag;

	@Schema(description = "目标功能id（当同步任务规则为自定义关联时需指定）")
	private String targetFunctionTag;

	@Schema(description = "测试用例id")
	private String testCaseId;

	@Schema(description = "数据类型：0-功能树三级数据，1-测试用例数据")
	private Integer dataType;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "修改时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;

//	@Schema(description = "是否删除：0-否，1-是")
//	private Integer deleted;


}