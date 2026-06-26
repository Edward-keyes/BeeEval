package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * 
 *
 * @email d2460687074@gmail.com
 * @date 2025-02-26 02:07:44
 */
@Data
@TableName("vehicle_domain_index")
@Accessors(chain = true)
public class DomainIndexEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 功能域id
	 */
	private Long functionalDomain;
	/**
	 * 指标名称
	 */
	private String indexName;
	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 定义
	 */
	private String definition;

	/**
	 * 定义 en
	 */
	private String definitionEn;

}
