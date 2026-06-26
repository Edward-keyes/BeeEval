package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* 流程状态结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@Schema(description = "流程状态结果")
public class ProcessStateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@Schema(description = "用户id")
	private Integer uid;

	@Schema(description = "测试状态id (test_state)")
	private Integer stateId;

	@Schema(description = "流程id")
	private Integer processId;

	@Schema(description = "是否成功 1成功 0失败")
	private Integer isSuccess;

	@Schema(description = "多选输入其他")
	private String other;

	@Schema(description = "标记问题 选项 1功能未搭载 2不具备测试条件 3用例未覆盖 4其他")
	private Integer errorSelect;

	@Schema(description = "标记问题 4其他输入框")
	private String error;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;


}