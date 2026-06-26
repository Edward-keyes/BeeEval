package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class ThreeData {

    /**
     * 三级标签id
     */
    private String threeId;
    /**
     * 三级标签名称
     */
    private String threeTagName;

    public ThreeData(String threeId, String threeTagName) {
        this.threeId = threeId;
        this.threeTagName = threeTagName;
    }
}
