package com.xailab.vehicle.operation.testplatform.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.xailab.vehicle.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* test_record查询
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "test_record查询")
public class TestPlatformVehicleTestRecordQuery extends Query {
}