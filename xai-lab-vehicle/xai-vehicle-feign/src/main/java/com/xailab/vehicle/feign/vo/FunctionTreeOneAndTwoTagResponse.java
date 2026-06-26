package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.util.List;


@Data
public class FunctionTreeOneAndTwoTagResponse {

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
     * 二级标签
     */
    private List<FunctionTwoTagVos> twoTagList;

}