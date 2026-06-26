package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-05-26 09:43:10
 */
@Data
@TableName("pcafe_relevancy_function_three_tag")
public class PcafeRelevancyFunctionThreeTagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * beeeval功能树三级标签编号
	 */
	private String beeevalThreeNumber;
	/**
	 * pecafe三级标签编号
	 */
	private String pecafeThreeNumber;

}
