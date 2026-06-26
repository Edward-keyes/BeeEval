package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.xailab.vehicle.feign.pojo.response.FunctionTreeCaseInfoResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeSyncAllTreeResponse
 * @Description:
 * @author: liulin
 * @date: 2025/6/8 19:22
 */
@Data
public class FunctionTreeSyncAllTreeResponse implements Serializable {
    /**
     * 功能标签
     */
    private String functionTag;

//    /**
//     * 场景任务
//     * 二级标签
//     */
//    private String scenarioTask;

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

//    /**
//     * 用例类型
//     * 0 :功能评价
//     * 1 :功能走查
//     */
//    private Integer tagType;

    /**
     * 测试用例详情
     */
    private List<TestPlatformFunctionTreeCaseResponse> testCaseInfo;


    /**
     * 对应beeeval 标签
     */
    /**
     * 标签编号
     */
    private String tagNumber;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 说明
     */
    private String description;

    /**
     * 功能数用例
     */
    private List<FunctionTreeCaseInfoResponse> functionTreeCase;


}
