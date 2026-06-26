package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeSyncTaskAuditRequest
 * @Description:
 * @author: liulin
 * @date: 2025/6/7 1:04
 */
@Data
public class FunctionTreeSyncTaskAuditRequest implements Serializable {
    /**
     * 任务流水号
     */
    @NotBlank(message = "任务流水号不能为空")
    private String taskSerial;

    /**
     * 审核结果
     * 0：不通过，1：通过
     */
    @NotNull(message = "审核结果不能为空")
    private Boolean auditResult;

    /**
     * 审核备注
     */
    private String auditRemark;

}
