package com.xailab.vehicle.operation.beeeval.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PcafeRelevancyFunctionThreeTagVos implements Serializable {

    /**
     *
     */
    private Long id;
    /**
     * beeeval功能树三级标签编号
     */
    private String beeevalThreeNumber;

    /**
     * beeeval功能树三级标签名称
     */
    private String beeevalThreeName;

    /**
     * pecafe三级标签id
     */
    private List<String> pecafeThreeNumber;

}