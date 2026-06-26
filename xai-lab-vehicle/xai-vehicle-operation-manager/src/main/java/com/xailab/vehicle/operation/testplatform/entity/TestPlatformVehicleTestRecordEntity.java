package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * test_record
 *
 * @author mm 
 * @since 1.0.0 2025-04-17
 */

@Data
@TableName("test_record")
public class TestPlatformVehicleTestRecordEntity {
	/**
	* 测试记录id
	*/
	@TableId
	private Integer id;

	/**
	* 用户id
	*/
	private Integer userId;

	/**
	* 车辆id
	*/
	private Integer vehicleId;

	/**
	* 创建时间
	*/
	private Date createTime;

	/**
	* 评分记录id
	*/
	private Integer scoreRecordId;

}