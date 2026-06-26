package com.xailab.vehicle.xaivehicledata.entity;

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
@TableName("vehicle_domain_tree")
@Accessors(chain = true)
public class DomainTreeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 功能域标签id
	 */
	private Long domainIndexId;

	/**
	 * 车基本信息id
	 */
	private Long vehicleBaseId;
	/**
	 * 测试时间
	 */
	private Date testDate;
	/**
	 * 展示用数值
	 */
	private Double presentationValue;
	/**
	 * 计算用评分
	 */
	private Double calculateScore;

}
