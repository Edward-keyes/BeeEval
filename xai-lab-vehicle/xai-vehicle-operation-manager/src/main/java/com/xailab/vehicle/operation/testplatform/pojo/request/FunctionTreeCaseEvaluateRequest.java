package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName: FunctionTreeCaseEditProcessRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 14:32
 */
@Data
public class FunctionTreeCaseEvaluateRequest {
    /**
     * 测试用例id
     */
    @NotNull(message = "测试用例id不能为空")
    private Integer id;

    /**
     * 测试用例 是否启用
     * true/false 启用/不启用
     */
    private Boolean isEnable;

    /**
     * 用例详情
     */
    @NotBlank(message = "测试用例不能为空")
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
