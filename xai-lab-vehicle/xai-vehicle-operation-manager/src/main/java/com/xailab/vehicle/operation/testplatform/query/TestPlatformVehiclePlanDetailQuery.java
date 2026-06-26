package com.xailab.vehicle.operation.testplatform.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.xailab.vehicle.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;


/**
* 方案细分表查询
*
* @author mumu
* @since 1.0.0 2025-04-16
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "方案细分表查询")
public class TestPlatformVehiclePlanDetailQuery extends Query {
}