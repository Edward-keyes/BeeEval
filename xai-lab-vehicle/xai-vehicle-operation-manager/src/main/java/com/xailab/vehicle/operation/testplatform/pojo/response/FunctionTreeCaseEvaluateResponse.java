package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

/**
 * @ClassName: FunctionTreeCaseEditProcessRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 14:32
 */

@Data
public class FunctionTreeCaseEvaluateResponse {
    /**
     * 测试用例id
     */
    private Integer id;

    /**
     * 测试用例 是否启用
     * true/false 启用/不启用
     */
    private Boolean isEnable;

    /**
     * 用例详情
     */
    private String testcaseContent;

    /**
     * 参考答案
     */
    private String referenceResult;

    /**
     * 考察点
     */
    private String inspectionPoint;



}
