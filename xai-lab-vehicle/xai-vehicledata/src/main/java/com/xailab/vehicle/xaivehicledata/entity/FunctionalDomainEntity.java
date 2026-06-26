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
@TableName("vehicle_functional_domain")
public class FunctionalDomainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 功能域名称
	 */
	private String functionalDomainName;
	/**
	 * 状态
	 */
	private Integer status;

}
