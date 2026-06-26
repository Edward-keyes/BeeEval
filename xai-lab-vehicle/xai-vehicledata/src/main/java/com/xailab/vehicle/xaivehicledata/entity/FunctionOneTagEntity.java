package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * 
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@Data
@TableName("vehicle_function_one_tag")
public class FunctionOneTagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 标签编号
	 */
	private String tagNumber;
	/**
	 * 标签名称
	 */
	private String tagName;

	/**
	 * 一级标签icon
	 */
	private String tagIcon;

}
