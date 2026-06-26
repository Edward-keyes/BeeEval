package com.xailab.vehicle.operation.testplatform.pojo.request;

import com.xailab.vehicle.framework.common.query.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @ClassName: TestPlatformFunctionTreeRequest
 * @Description:
 * @author: liulin
 * @date: 2025/4/28 22:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TestPlatformFunctionTreeRequest extends Query implements Serializable {

    /**
     * 测试任务id
     */
    private Integer testRecordId;

    /**
     * 分类
     */
    private String type;

    /**
     * 功能域id
     */
    private Integer functionId;

    /**
     * TODO：功能评价
     * na/modest/avg/good
     * 没有/中等/平均/好
     * {@link com.xailab.vehicle.operation.testplatform.enums.FunctionTreeEvaluteStateEnum}
     */
    private String functionEvaluate;

    /**
     * 数据状态
     * 0/1/2
     * 缺数据/可同步/待审核
     * {@link com.xailab.vehicle.operation.testplatform.enums.FunctionTreeDataStateEnum}
     */
    private Integer dataState;

    /**
     * 用例id
     */
    private Integer testCaseId;



}
