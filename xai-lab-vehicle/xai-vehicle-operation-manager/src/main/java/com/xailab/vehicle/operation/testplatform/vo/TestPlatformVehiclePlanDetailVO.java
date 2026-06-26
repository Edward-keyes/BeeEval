package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
* 方案细分表
*
* @author mumu
* @since 1.0.0 2025-04-16
*/
@Data
@Schema(description = "方案细分表")
public class TestPlatformVehiclePlanDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@Schema(description = "详细测试方案名称")
	private String planDetailName;

	private Integer testPlanId;


}