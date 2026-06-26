package com.xailab.vehicle.operation.testplatform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 功能评价数据表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */

@Data
@TableName("test_ques")
public class TestQuesEntity {
	@TableId
	private Integer id;

	/**
	* 三级名称
	*/
	private String testName;

	/**
	* 选项类型 0单选 1多选
	*/
	private Integer optionsType;

	/**
	* 类型
	*/
	private String categoryType;

	/**
	* 问题/维度
	*/
	private String questionDimension;

	/**
	 * 状态 0正常 1 停用
	 */
	private Integer state;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime createTime;

}