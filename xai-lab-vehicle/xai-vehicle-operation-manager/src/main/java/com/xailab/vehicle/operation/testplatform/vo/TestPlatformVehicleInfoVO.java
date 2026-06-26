package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* 车辆信息表
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@Data
@Schema(description = "车辆信息表")
public class TestPlatformVehicleInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "车辆id")
	private Integer id;
	@Schema(description = "车辆id")
	private Integer vehicleId;

	@Schema(description = "车型名称")
	private String vehicleName;

	@Schema(description = "车机软件版本")
	private String infotainmentSoftwareVersion;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;


}