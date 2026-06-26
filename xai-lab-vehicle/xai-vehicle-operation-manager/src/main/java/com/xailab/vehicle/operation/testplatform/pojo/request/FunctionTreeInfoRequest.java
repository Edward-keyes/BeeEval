package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeInfoRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/18 23:05
 */
@Data
public class FunctionTreeInfoRequest implements Serializable {
//    /**
//     * 功能域id(对应plan_detail表)
//     */
//    @NotNull(message = "功能域id")
//    private Integer functionDomainId;

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


}
