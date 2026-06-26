package com.xailab.vehicle.feign.vo;

import lombok.Data;

@Data
public class DomainRelevancyVo {

    private String functionalDomainName;

    private Long functionIndexId;

    private String indexName;

    private String testCaseContent;
}
