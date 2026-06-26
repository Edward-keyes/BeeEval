package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName: TestCaseStateErrorRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/5 16:38
 */
@Data
public class TestCaseStateErrorRequest {
    /**
     * state id
     * 状态id
     */
    private Integer stateId;


//    /**
//     * 测试用例类型
//     * 0：功能评价
//     * 1：功能走查
//     */
//    @NotNull(message = "测试用例类型不能为空")
//    private Integer caseType;

    /**
     * 错误类型
     */
    private Integer errorType;

    /**
     * 错误说明
     */
    private String errorDetail;
}
