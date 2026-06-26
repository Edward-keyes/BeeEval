package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-05-26 09:43:10
 */

@Data
public class PcafeRelevancyFunctionThreeTagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long id;
	/**
	 * beeeval功能树三级标签编号
	 */
	private String beeevalThreeNumber;


	/**
	 * beeeval功能树三级标签名称
	 */
	private String beeevalThreeName;

	/**
	 * pecafe三级标签id
	 */
	private String pecafeThreeNumber;

}
