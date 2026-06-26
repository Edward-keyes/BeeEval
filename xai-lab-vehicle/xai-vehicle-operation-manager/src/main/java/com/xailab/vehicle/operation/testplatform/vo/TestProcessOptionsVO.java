package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;

/**
* 流程选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@Schema(description = "流程选项表")
public class TestProcessOptionsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@Schema(description = "流程id")
	private Integer processId;

	@Schema(description = "选项")
	private String options;

	/**
	 * 排序，序列从1开始
	 */
	private Integer sort;


}