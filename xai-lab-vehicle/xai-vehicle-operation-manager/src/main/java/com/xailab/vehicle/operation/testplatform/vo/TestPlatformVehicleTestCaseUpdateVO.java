package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
* 测试用例总表
*
* @author mumu 
* @since 1.0.0 2025-04-16
*/
@Data
@Schema(description = "测试用例总表")
public class TestPlatformVehicleTestCaseUpdateVO implements Serializable {

	/**
	 * id
	 */
	@Schema(description = "测试用例id")
	@NotNull(message = "id不能为空")
	private Integer id;

	/**
	 * 用例内容
	 */
	@Schema(description = "用例内容")
	@NotBlank(message = "用例内容不能为空")
	private String testcaseContent;

	/**
	 * 一级指标
	 */
	@Schema(description = "一级指标")
	private String primaryMetric;

	/**
	 * 二级指标
	 */
	@Schema(description = "二级指标")
	private String secondaryMetric;

	/**
	 * 三级指标
	 */
	@Schema(description = "三级指标")
	private String tertiaryMetric;

	/**
	 * 功能域id
	 */
	@Schema(description = "功能域id")
	private Integer functionId;

	/**
	 * 场景id
	 */
	@Schema(description = "场景id")
	private Integer scenarioId;

	/**
	 * 场景任务
	 */
	@Schema(description = "场景任务")
	private String scenarioTask;

	/**
	 * 任务细分
	 */
	@Schema(description = "任务细分")
	private String taskDetail;

	/**
	 * 计分方式
	 */
	@Schema(description = "计分方式")
	private String scoringMethod;

	/**
	 * 评分标准
	 */
	@Schema(description = "评分标准")
	private String scoringCriteria;


}