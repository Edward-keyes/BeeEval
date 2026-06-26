package com.xailab.vehicle.feign.vo;

import lombok.Data;

@Data
public class TestCaseByFunctionIdVo {

    /**
     * 用例id
     */
    private String caseId;

    /**
     * 功能ID
     */
    private String taskDetail;

    /**
     * 用例数据
     */
    private String caseContent;

    /**
     * 用例数据en
     */
    private String caseContentEm;

}
