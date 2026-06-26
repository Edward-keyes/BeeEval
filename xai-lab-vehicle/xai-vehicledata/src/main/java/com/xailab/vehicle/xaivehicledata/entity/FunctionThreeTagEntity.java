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
@TableName("vehicle_function_three_tag")
public class FunctionThreeTagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	/**
	 * 二级标签id
	 */
	private Long functionTwoTagId;
	/**
	 * 标签名称
	 */
	private String tagName;
	/**
	 * 标签名称En
	 */
	private String tagNameEn;
	/**
	 * 标签编号
	 */
	private String tagNumber;
	/**
	 * 说明
	 */
	private String description;
	/**
	 * 说明En
	 */
	private String descriptionEn;
	/**
	 * 状态
	 * -1:停用
	 * 0:待审核
	 * 1:已上线
	 */
	private Integer status;
	/**
	 * 任务流水号
	 */
	private String taskSerial;
	/**
	 * 排序
	 */
	private Integer sort;
}
