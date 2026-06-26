package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionTreeOneAndTwoTagVo {

    /**
     * 一级标签id
     */
    private Long oneTagId;
    /**
     * 标签编号
     */
    private String oneTagNumber;
    /**
     * 一级标签名称
     */
    private String oneTagName;
    /**
     * 二级标签id
     */
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
     * 三级标签id
     */
    private String threeTagId;
    /**
     * 标签编号
     */
    private String threeTagNumber;
    /**
     * 标签名称
     */
    private String threeTagName;
}
