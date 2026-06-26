package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.util.List;

@Data
public class FunctionTwoTagVos {

    private Long twoTagId;
    /**
     * 标签编号
     */
    private String twoTagNumber;
    /**
     * 标签名称
     */
    private String twoTagName;

    /**
     * 三级list
     */
    private List<FunctionThreeTagVos> threeTagVos;

}
