package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* 测试记录用例表
*
* @author mumu 
* @since 1.0.0 2025-04-16
*/
@Data
@Schema(description = "测试记录用例表")
public class TestPlatformVehicleTestStateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@Schema(description = "测试状态 未测试：0；已测试：1")
	private Integer testStatus;

	@Schema(description = "测试记录id")
	private Integer recordId;

	@Schema(description = "测试用例id")
	private Integer testcaseId;

	@Schema(description = "任务是否成功 初始：-1； 失败：0； 成功：1")
	private Integer isSuccessful;

	@Schema(description = "打分")
	private Integer score;

	private Integer hasBug;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "音频路径")
	private String audioPath;

	@Schema(description = "错误类型")
	private Integer errorType;

	@Schema(description = "错误详情")
	private String errorDetail;

	@Schema(description = "case类型")
	private Integer caseType;


}