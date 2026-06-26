package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

@Data
public class FunctionTreeOpResponse {

    /**
     * 一级标签
     */
    private String oneTagName;

    /**
     * 一级标签En
     */
    private String oneTagNameEn;

    /**
     * 二级标签
     */
    private String twoTagName;

    /**
     * 二级标签En
     */
    private String twoTagNameEn;

    /**
     * 三级标签
     */
    private String tagName;

    /**
     * 三级标签En
     */
    private String tagNameEn;

    /**
     * 三级标签编号
     */
    private String tagNumber;

    /**
     * 三级标签描述
     */
    private String description;

    /**
     * 三级标签描述En
     */
    private String descriptionEn;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;
}
