package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FunctionTreeCaseFeignVo {

    private Long id;

    private String ThreeTagId;

    private String caseContent;

    private String caseContentEn;

    private Date createTime;

    private Date updateTime;

}
