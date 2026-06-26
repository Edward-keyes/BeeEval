package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeStateEditRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/18 22:37
 */
@Data
public class FunctionTreeStateEditRequest implements Serializable {
//    /**
//     * 功能域id(对应plan_detail表)
//     */
//    @NotNull(message = "功能域id")
//    private Integer functionDomainId;

    /**
     * 测试任务id
     */
    @NotNull(message = "测试任务id不能为空")
    private Integer recordId;

//    /**
//     * 场景任务
//     */
//    @NotBlank(message = "场景任务不能为空")
//    private String scenarioTask;
//
    /**
     * 任务细分
     */
    @NotBlank(message = "功能标签不能为空")
    private String taskDetail;

    /**
     * 功能评价状态 na/modest/avg/good
     */
    private String functionEvaluate;

    /**
     * 数据状态 0/1/2 缺数据/可同步/待审核
     */
    private Integer dataState;

}
