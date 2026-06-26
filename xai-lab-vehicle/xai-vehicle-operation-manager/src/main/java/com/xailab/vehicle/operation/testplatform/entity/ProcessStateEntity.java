package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 流程状态结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */

@Data
@TableName("process_state")
public class ProcessStateEntity {
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
	* 流程id
	*/
	private Integer processId;

	/**
	* 是否成功 1成功 0失败
	*/
	private Integer isSuccess;

	/**
	* 多选输入其他
	*/
	private String other;

	/**
	* 标记问题 选项 1功能未搭载 2不具备测试条件 3用例未覆盖 4其他
	*/
	private Integer errorSelect;

	/**
	* 标记问题 4其他输入框
	*/
	private String error;

	/**
	* 创建时间
	*/
	private Date createTime;

}