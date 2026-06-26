package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 问卷选项 选择结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */

@Data
@TableName("ques_state_options")
public class QuesStateOptionsEntity {
	@TableId
	private Integer id;

	/**
	* 功能评价 问卷状态id
	*/
	private Integer quesStateId;

	/**
	* 选择的id
	*/
	private Integer selectId;

	/**
	 * 数据状态
	 * na ：预设
	 * good：Good
	 * poor：Poor
	 */
	private String dataState;

}