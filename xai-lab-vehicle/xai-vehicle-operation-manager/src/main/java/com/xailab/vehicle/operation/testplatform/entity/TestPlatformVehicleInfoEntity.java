package com.xailab.vehicle.operation.testplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 车辆信息表
 *
 * @author mm 
 * @since 1.0.0 2025-04-17
 */

@Data
@TableName("vehicle")
public class TestPlatformVehicleInfoEntity {
	/**
	* 车辆id
	*/
	@TableId
	private Integer id;

	/**
	* 车型名称
	*/
	private String vehicleName;

	/**
	* 车机软件版本
	*/
	private String infotainmentSoftwareVersion;

	/**
	* 创建时间
	*/
	private Date createTime;

}