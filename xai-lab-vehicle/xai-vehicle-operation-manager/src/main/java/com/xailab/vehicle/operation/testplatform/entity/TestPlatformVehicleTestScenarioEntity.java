package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 测试场景表
 *
 * @author mumu
 * @since 1.0.0 2025-04-16
 */

@Data
@TableName("test_scenario")
public class TestPlatformVehicleTestScenarioEntity {
	@TableId
	private Integer id;

	/**
	* 场景名
	*/
	private String scenarioName;

}