package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 功能评价选项表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */

@Data
@TableName("test_ques_options")
public class TestQuesOptionsEntity {
	@TableId
	private Integer id;

	/**
	* 问题id
	*/
	private Integer quesId;

	/**
	* 选项
	*/
	private String options;

	/**
	* 排序
	*/
	private Integer sort;

}