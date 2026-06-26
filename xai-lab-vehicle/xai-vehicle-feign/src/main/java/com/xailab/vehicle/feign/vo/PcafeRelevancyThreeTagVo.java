package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.util.List;

@Data
public class PcafeRelevancyThreeTagVo {

    /**
     *
     */
    private Long id;
    /**
     * beeeval功能树三级标签编号
     */
    private String beeevalThreeNumber;
    /**
     * pecafe三级标签id
     */
    private String pecafeThreeNumber;
    /**
     * beeeval功能树三级标签名称
     */
    private String beeevalThreeName;
}
