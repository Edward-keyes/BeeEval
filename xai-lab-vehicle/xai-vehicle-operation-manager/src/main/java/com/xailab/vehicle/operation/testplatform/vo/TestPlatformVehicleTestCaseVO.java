package com.xailab.vehicle.operation.testplatform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* 测试用例总表
*
* @author mumu 
* @since 1.0.0 2025-04-16
*/
@Data
@Schema(description = "测试用例总表")
public class TestPlatformVehicleTestCaseVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "测试用例id")
	private Integer id;

	@Schema(description = "用例内容")
	private String testcaseContent;

	@Schema(description = "一级指标")
	private String primaryMetric;

	@Schema(description = "二级指标")
	private String secondaryMetric;

	@Schema(description = "三级指标")
	private String tertiaryMetric;

	@Schema(description = "功能域id")
	private Integer functionId;

	@Schema(description = "功能域id名称")
	private String functionName;

	@Schema(description = "场景id")
	private Integer scenarioId;
	/**
	 * 场景名称
	 */
	@Schema(description = "场景名称")
	private String scenarioName;

	@Schema(description = "场景任务")
	private String scenarioTask;

	@Schema(description = "任务细分")
	private String taskDetail;

	@Schema(description = "计分方式")
	private String scoringMethod;

	@Schema(description = "评分标准")
	private String scoringCriteria;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	/**
	 * 行业平均 评分
	 */
	@Schema(description = "行业平均评分")
	private BigDecimal industryAverage;

	/**
	 * 评分方差值
	 */
	@Schema(description = "评分方差值")
	private BigDecimal varianceValue;


	/**
	 * 多个车辆信息,和评分 一个车辆一列
	 */
	@Schema(description = "多个车辆信息,和评分")
	private List<TestPlatformVehicleCorePageVo> vehicleInfos;

	/**
	 * 指定车辆之间的方差
	 */
	private BigDecimal vehicleVariance;

}