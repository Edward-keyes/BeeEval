package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FunctionTreeCaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 三级标签id or 功能id
     */
    private String threeTagId;

    /**
     * 用例内容
     */
    private String caseContent;

    /**
     * 用例内容 en
     */
    private String caseContentEn;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}