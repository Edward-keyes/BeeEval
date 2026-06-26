package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import com.xailab.vehicle.framework.mybatis.entity.BaseEntity;

/**
 * 测试用例总表
 *
 * @author mumu 
 * @since 1.0.0 2025-04-16
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("test_case")
public class TestPlatformVehicleTestCaseEntity{
	/**
	 * id
	 */
	@TableId
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
	 * 参考答案
	 */
	private String referenceResult;

	/**
	 * 考察点
	 */
	private String inspectionPoint;

	/**
	 * 是否启用
	 * true/false
	 */
	private Boolean isEnable;

}