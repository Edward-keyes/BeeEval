package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 功能评价结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */

@Data
@TableName("ques_state")
public class QuesStateEntity {
	@TableId
	private Integer id;

	/**
	* 用户id
	*/
	private Integer uid;

	/**
	* 测试状态id (test_state)
	*/
	private Integer stateId;

	/**
	* 问题id
	*/
	private Integer quesId;

	/**
	* 其他 输入框
	*/
	private String other;

	/**
	* 选项类型 0单选 1多选
	*/
	private Integer optionType;

	/**
	* 创建时间
	*/
	private Date createTime;

}