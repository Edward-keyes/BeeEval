package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: TestPlatformFunctionTreeResponse
 * @Description:
 * @author: liulin
 * @date: 2025/4/28 22:23
 */
@Data
public class FunctionTreeSyncTreeListResponse implements Serializable {

    /**
     * 功能标签
     */
    private String functionTag;

    /**
     * 场景任务
     * 二级标签
     */
    private String scenarioTask;

    /**
     * TODO：功能评价
     * na/modest/avg/good
     * 没有/中等/平均/好
     */
    private String functionEvaluate;

    /**
     * 数据状态
     * 0/1/2
     * 缺数据/可同步/待审核
     */
    private Integer dataState;

    /**
     * 用例类型
     * 0 :功能评价
     * 1 :功能走查
     */
    private Integer tagType;

//    /**
//     * 是否选中
//     */
//    private Boolean isSelect;

    /**
     * 目标功能标签
     */
    private String targetFunctionTag;

    /**
     * 测试用例详情
     */
    private List<TestPlatformFunctionTreeCaseResponse> testCaseInfo;


}
