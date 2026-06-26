package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 功能树状态表
 *
 * @author mumu 
 * @since 1.0.0 2025-05-18
 */

@Data
@TableName("function_tree_state")
public class FunctionTreeStateEntity {
	/**
	* 功能树状态id
	*/
	@TableId
	private Long id;

	/**
	* 功能域id(对应plan_detail表)
	*/
	private Integer functionDomainId;

	/**
	* 测试任务id
	*/
	private Integer recordId;

	/**
	* 场景任务
	*/
	private String scenarioTask;

	/**
	* 任务细分
	*/
	private String taskDetail;

	/**
	* 功能评价状态 na/modest/avg/good
	*/
	private String functionEvaluate;

	/**
	* 数据状态 0/1/2 缺数据/可同步/待审核
	*/
	private Integer dataState;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	* 修改时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	* 是否删除 0/1 否/是
	*/
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;

}