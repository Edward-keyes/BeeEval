package com.xailab.vehicle.feign.pojo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeCaseInfoResponse
 * @Description:
 * @author: liulin
 * @date: 2025/6/8 1:30
 */
@Data
public class FunctionTreeCaseInfoResponse implements Serializable {
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


}
