package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionDomainIndexVo {

    /**
     * 功能域名称
     */
    private String functionalDomainName;

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
