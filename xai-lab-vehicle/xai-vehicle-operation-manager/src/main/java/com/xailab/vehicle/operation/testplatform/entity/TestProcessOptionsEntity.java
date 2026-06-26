package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 流程选项表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */

@Data
@TableName("test_process_options")
public class TestProcessOptionsEntity {
	@TableId
	private Integer id;

	/**
	* 流程id
	*/
	private Integer processId;

	/**
	* 选项
	*/
	private String options;

	private Integer sort;

}