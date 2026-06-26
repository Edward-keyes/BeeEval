package com.xailab.vehicle.feign.vo;

import lombok.Data;

@Data
public class FunctionThreeTagVos {
    private Long threeTagId;
    /**
     * 标签编号
     */
    private String threeTagNumber;
    /**
     * 标签名称
     */
    private String threeTagName;

    public FunctionThreeTagVos(String threeTagId, String threeTagNumber, String threeTagName) {
        this.threeTagId = Long.parseLong(threeTagId);
        this.threeTagNumber = threeTagNumber;
        this.threeTagName = threeTagName;
    }
}
