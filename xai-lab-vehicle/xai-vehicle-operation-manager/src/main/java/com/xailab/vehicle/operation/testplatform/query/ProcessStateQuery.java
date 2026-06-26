package com.xailab.vehicle.operation.testplatform.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.xailab.vehicle.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 流程状态结果查询
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "流程状态结果查询")
public class ProcessStateQuery extends Query {
}