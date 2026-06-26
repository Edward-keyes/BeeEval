package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* 功能评价结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@Schema(description = "功能评价结果")
public class QuesStateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@Schema(description = "用户id")
	private Integer uid;

	@Schema(description = "测试状态id (test_state)")
	private Integer stateId;

	@Schema(description = "问题id")
	private Integer quesId;

	@Schema(description = "其他 输入框")
	private String other;

	@Schema(description = "选项类型 0单选 1多选")
	private Integer optionType;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;


}