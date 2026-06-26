package com.xailab.vehicle.operation.testplatform.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 流程数据表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */

@Data
@TableName("test_process")
public class TestProcessEntity {
	/**
	* 流程id
	*/
	@TableId
	private Integer id;

	/**
	* 一级
	*/
	private String levelOneName;

	/**
	* 二级
	*/
	private String levelTwoName;

	/**
	* 三级
	*/
	private String levelThreeName;

	/**
	* 步骤顺序
	*/
	private Integer step;

	/**
	* 流程题目
	*/
	private String processTitle;

	/**
	* 流程描述
	*/
	private String description;

	/**
	* 提示
	*/
	private String tip;

	/**
	* 问题1
	*/
	private String quesOne;

	/**
	* 问题2
	*/
	private String quesTwo;

	/**
	* 问题3
	*/
	private String quesThree;

	/**
	* 选项标题
	*/
	private String optionsTitle;

	/**
	* 选项类型
	*/
	@Schema(description = "选项类型 0单选 1多选")
	private String optionsType;

}