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
public class TestPlatformFunctionTreeResponse implements Serializable {

    /**
     * 标识
     */
    private String tag;
    /**
     * 功能id
     */
    private String functionTagId;

    /**
     * 功能标签名称
     */
    private String functionTagName;

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
     * 域id
     */
    private Integer functionId;

    /**
     * 域名称
     */
    private String functionDomainName;


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


    /**
     * 测试用例详情
     */
    private List<TestPlatformFunctionTreeCaseResponse> testCaseInfo;


}
