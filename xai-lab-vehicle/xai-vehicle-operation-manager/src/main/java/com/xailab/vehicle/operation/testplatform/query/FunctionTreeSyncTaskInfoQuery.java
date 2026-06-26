package com.xailab.vehicle.operation.testplatform.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.xailab.vehicle.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 功能树同步任务详情表查询
*
* @author mm 
* @since 1.0.0 2025-06-02
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "功能树同步任务详情表查询")
public class FunctionTreeSyncTaskInfoQuery extends Query {
}