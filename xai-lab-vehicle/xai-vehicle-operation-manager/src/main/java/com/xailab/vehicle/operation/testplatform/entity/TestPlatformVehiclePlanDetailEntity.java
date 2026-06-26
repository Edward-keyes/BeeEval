package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 方案细分表
 *
 * @author mumu
 * @since 1.0.0 2025-04-16
 */

@Data
@TableName("plan_detail")
public class TestPlatformVehiclePlanDetailEntity {
	@TableId
	private Integer id;

	/**
	* 详细测试方案名称
	*/
	private String planDetailName;

	private Integer testPlanId;

	/**
	* 测试用例序列ID
	 * 车控域是1xxxx，出行2xxxx，车书3xxxx，娱乐4xxxx，闲聊5xxxx，创作6xxxx
	*/
	private Integer caseSerialId;

}