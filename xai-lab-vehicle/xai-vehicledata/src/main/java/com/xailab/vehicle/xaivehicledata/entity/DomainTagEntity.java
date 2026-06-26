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
 * @date 2025-02-26 02:07:44
 */
@Data
@TableName("vehicle_domain_tag")
public class DomainTagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 功能域id
	 */
	private Long functionalDomainId;
	/**
	 * 标签名称
	 */
	private String tagName;
	/**
	 * 状态
	 */
	private Integer status;

}
