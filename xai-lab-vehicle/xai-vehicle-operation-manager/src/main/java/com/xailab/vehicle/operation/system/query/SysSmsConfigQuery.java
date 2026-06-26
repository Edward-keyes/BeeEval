package com.xailab.vehicle.operation.system.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.xailab.vehicle.framework.common.query.Query;

/**
 * 短信配置查询
 *
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "短信配置查询")
public class SysSmsConfigQuery extends Query {
    @Schema(description = "平台类型")
    private Integer platform;

    @Schema(description = "分组名称")
    private String groupName;

}