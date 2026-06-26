package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.util.List;

@Data
public class FunctionTreeSynchronizationRequest {

    /**
     * 未同步功能id
     */
    private String functionId;

    /**
     * 同步至beeeva哪个二级标签id
     */
    private Long secondTagId;

    /**
     * 功能id标签名称
     */
    private String functionTagName;

    /**
     * 功能id标签名称 en
     */
    private String functionTagNameEn;

    /**
     * 用例数据
     */
    private List<TestCaseByFunctionIdVo> caseInfoResponseList;

}
