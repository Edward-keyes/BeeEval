package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionDomainIndexMapVo {
    /**
     * 功能域id
     */
    private Long functionalId;

    /**
     * 功能域指标Id
     */
    private Long domainIndexId;

    /**
     * 指标名称
     */
    private String indexName;
}
