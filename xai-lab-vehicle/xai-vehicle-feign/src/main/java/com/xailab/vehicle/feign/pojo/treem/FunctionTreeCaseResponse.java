package com.xailab.vehicle.feign.pojo.treem;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeCaseInfoResponse
 * @Description:
 * @author: liulin
 * @date: 2025/6/8 1:30
 */
@Data
public class FunctionTreeCaseResponse implements Serializable {
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
     * 是否首页展示
     */
    private Boolean isShow;

    /**
     * 测试结果 0: na/1:modest/2:avg/3:good
     */
    private Integer functionEvaluation;

    /**
     * 用例选项 结果
     */
    private String caseOptions;

}
