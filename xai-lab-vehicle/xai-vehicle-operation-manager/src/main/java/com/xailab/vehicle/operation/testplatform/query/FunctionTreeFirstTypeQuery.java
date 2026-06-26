package com.xailab.vehicle.operation.testplatform.query;

import com.xailab.vehicle.framework.common.query.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
* 功能树一级标签类型查询
*
* @author mu 
* @since  2025-05-31
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "功能树一级标签类型查询")
public class FunctionTreeFirstTypeQuery extends Query {
}