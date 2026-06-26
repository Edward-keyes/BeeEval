package com.xailab.vehicle.operation.testplatform.pojo.request;

import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeCaseProcessInfo;
import lombok.Data;

/**
 * @ClassName: FunctionTreeCaseEditProcessRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 14:32
 */

@Data
public class FunctionTreeCaseProcessRequest {
    /**
     * 测试用例id
     */
    private Integer id;
    /**
     * 测试用例类型
     * 0：功能评价
     * 1：功能走查
     */
    private Integer caseType;

    /**
     * 测试用例类型为 功能评价 展示
     * 参考答案
     */
    private String referenceResult;

    /**
     * 测试用例类型为 功能评价 展示
     * 考察点
     */
    private String inspectionPoint;


    /**
     * 测试用例类型为 功能走查 展示
     * 题目选项详情
     */
    private FunctionTreeCaseProcessInfo processInfo;


}
