package com.xailab.vehicle.xaivehicledata.entity;

import lombok.Data;

@Data
public class ThreeTagList {

    /**
     * 三级标签
     */
    private String domain;

    /**
     * 主标签
     */
    private String mainDomain;

    /**
     * 覆盖车辆数
     */
    private Integer count;

    /**
     * 覆盖率
     */
    private Double penetration;
}
