package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */

@Data
@TableName("error_type")
public class ErrorTypeEntity {
	private Integer id;

	/**
	* 错误的名称
	*/
	private String errorName;

	/**
	* 错误的类型 0是旧ui 1是新ui功能评价
	*/
	private Integer errorType;

}