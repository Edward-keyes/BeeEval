package com.xailab.vehicle.operation.testplatform.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: PcafeRelevancyFunctionThreeTagVo
 * @Description:
 * @author: liulin
 * @date: 2025/6/3 21:58
 */
@Data
public class PcafeRelevancyFunctionThreeTagVo implements Serializable {

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
}
