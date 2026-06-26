package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

@Data
public class FunctionalDomainResponse {

    private String id;

    /**
     * 功能域名称
     */
    private String functionalDomainName;

    /**
     * 状态
     */
    private Integer status;

}