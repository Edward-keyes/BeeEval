package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: TestPlatformFunctionTreeCaseResponse
 * @Description:
 * @author: liulin
 * @date: 2025/4/28 22:31
 */
@Data
public class TestPlatformFunctionTreeCaseResponse implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 用例详情
     */
    private String testcaseContent;

    /**
     * 场景任务 二级标签
     */
    private String scenarioTask;

    /**
     * 任务细分 三级标签
     */
    private String taskDetail;

    /**
     * 功能域id(对应plan_detail表)
     */
    private Integer functionId;

    /**
     * 域名称
     */
    private String functionDomainName;

    /**
     * 测试用例评级
     * 未验证/Avg/Good/Poor
     * na/avg/good/poor
     *  @see  FunctionTreeCaseRateEnum
     */
    private String testCaseRate;

    /**
     * 测试用例素材状态
     * @see  FunctionTreeResultMeterialEnum
     */
    private String materialState;

    /**
     * 用例类型
     * 0 :功能评价
     * 1 :功能走查
     */
    private Integer caseType;
}
