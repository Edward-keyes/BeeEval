package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName: TestCaseOptionsInfoRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/5 15:54
 */
@Data
public class TestCaseOptionsInfoRequest {
    /**
     * 问题id
     */
    private Integer questionId;


    /**
     * 测试用例类型
     * 0：功能评价
     * 1：功能走查
     */
    @NotNull(message = "测试用例类型不能为空")
    private Integer caseType;

}
