package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;

/**
* 问卷选项 选择结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@Schema(description = "问卷选项 选择结果")
public class QuesStateOptionsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@Schema(description = "功能评价 问卷状态id")
	private Integer quesStateId;

	@Schema(description = "选择的id")
	private Integer selectId;

	/**
	 * 数据状态
	 * na ：预设
	 * good：Good
	 * poor：Poor
	 */
	@Schema(description = "数据状态")
	private String dataState;


}