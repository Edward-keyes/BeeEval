package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionThreeTagVo {

    /**
     * 三级标签id
     */
    private String id;

    /**
     * 三级标签名称
     */
    private String tagName;

    public FunctionThreeTagVo(String id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }
}
