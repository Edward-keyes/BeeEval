package com.xailab.vehicle.operation.testplatform.entity;

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
 * @date 2025-06-22 16:36:24
 */
@Data
@TableName("tertiary_metric_weight")
public class TertiaryMetricWeightEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 三级指标
	 */
	private String tertiaryMetric;
	/**
	 * 权重
	 */
	private Double weight;
	/**
	 * 类型
	 */
	private Integer type;

}
