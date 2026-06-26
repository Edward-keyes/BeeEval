package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 流程选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@Schema(description = "流程选项表")
public class TestProcessOptionsStateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * option_state_id
	 */
	private Integer id;

	@Schema(description = "流程状态id")
	private Integer processStateId;

	@Schema(description = "多选 选择的id")
	private Integer selectId;

	/**
	 * 数据状态
	 * na ：预设
	 * good：Good
	 * poor：Poor
	 */
	private String dataState;


	@Schema(description = "选项")
	private String options;

	/**
	 * 排序，序列从1开始
	 */
	private Integer sort;


}