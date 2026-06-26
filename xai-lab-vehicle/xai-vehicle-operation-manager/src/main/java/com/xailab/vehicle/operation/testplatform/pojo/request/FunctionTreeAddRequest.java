package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeAddRequest
 * @Description: 功能树新增
 * @author: liulin
 * @date: 2025/5/3 0:17
 */
@Data
public class FunctionTreeAddRequest implements Serializable {

    /**
     * 域id
     */
    @NotNull(message = "域id不能为空")
    private Integer functionId;

    /**
     * 用例类型
     * 0 :功能评价
     * 1 :功能走查
     */
    @NotNull(message = "用例类型不能为空")
    private Integer tagType;
    /**
     * 一级指标类型
     */
    @NotBlank(message = "一级类型不能为空")
    private String firstlyType;

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
    @NotBlank(message = "任务详情不能为空")
    private String taskDetail;



    /**
     * 默认测试用例
     */
    @NotBlank(message = "测试用例不能为空")
    private String testcaseContent;

    /**
     * 测试任务id
     */
//    @NotNull(message = "测试任务id不能为空")
    private Integer testRecordId;


}
