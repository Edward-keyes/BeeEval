package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import java.util.Date;

/**
* test_record
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@Data
@Schema(description = "test_record")
public class TestPlatformVehicleTestRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "测试记录id")
	private Integer id;

	@Schema(description = "用户id")
	private Integer userId;

	@Schema(description = "车辆id")
	private Integer vehicleId;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "评分记录id")
	private Integer scoreRecordId;


}