package com.xailab.vehicle.operation.testplatform.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 测试用例总表
 *
 * @author mumu 
 * @since 1.0.0 2025-04-16
 */
@Data
public class TestPlatformTestCasePageVo implements Serializable {
	/**
	 * id
	 */
	private Integer id;

	/**
	* 用例内容
	*/
	private String testcaseContent;

	/**
	* 一级指标
	*/
	private String primaryMetric;

	/**
	* 二级指标
	*/
	private String secondaryMetric;

	/**
	* 三级指标
	*/
	private String tertiaryMetric;

	/**
	* 功能域id(对应plan_detail表)
	*/
	private Integer functionId;

	/**
	* 场景id
	*/
	private Integer scenarioId;

	/**
	* 场景任务
	*/
	private String scenarioTask;

	/**
	* 任务细分
	*/
	private String taskDetail;

	/**
	* 计分方式
	*/
	private String scoringMethod;

	/**
	* 评分标准
	*/
	private String scoringCriteria;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 行业平均 评分
	 */
	private Double industryAverage;

	/**
	 * 评分方差值
	 */
	private Double varianceValue;


}