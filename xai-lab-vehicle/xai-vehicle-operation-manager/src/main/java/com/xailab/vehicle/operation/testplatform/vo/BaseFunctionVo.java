package com.xailab.vehicle.operation.testplatform.vo;

import lombok.Data;

@Data
public class BaseFunctionVo {

    private String vehicleName;

    private String functionName;

    private String indexName;

    private Double value;

    /**
     * 1.认知能力
     * 2.行动能力
     */
    private Integer type;

    /**
     * unit 单位
     */
    private String unit;
}
