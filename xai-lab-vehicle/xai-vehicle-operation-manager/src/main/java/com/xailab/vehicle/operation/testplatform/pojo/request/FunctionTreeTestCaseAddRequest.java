package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeTestCaseAddRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 1:48
 */
@Data
public class FunctionTreeTestCaseAddRequest implements Serializable {

    /**
     * 测试用例内容
     */
    @NotBlank(message = "测试用例内容不能为空")
    private String testcaseContent;

    /**
     * 域id
     */
    @NotNull(message = "域id不能为空")
    private Integer functionId;

    /**
     * 场景任务
     * 二级标签
     */
    @NotBlank(message = "场景任务不能为空")
    private String scenarioTask;

    /**
     * 任务详情
     * 三级标签
     */
    @NotBlank(message = "功能标签不能为空")
    private String functionTag;


    /**
     * 测试任务id
     */
//    @NotNull(message = "测试任务id不能为空")
    private Integer testRecordId;



}
