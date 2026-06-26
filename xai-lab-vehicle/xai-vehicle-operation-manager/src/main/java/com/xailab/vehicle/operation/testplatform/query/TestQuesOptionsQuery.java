package com.xailab.vehicle.operation.testplatform.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.xailab.vehicle.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;


/**
* 功能评价选项表查询
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "功能评价选项表查询")
public class TestQuesOptionsQuery extends Query {
}