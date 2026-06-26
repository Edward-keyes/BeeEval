package com.xailab.vehicle.operation.testplatform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: FunctionTreeSyncAuditJournalVO
 * @Description:
 * @author: liulin
 * @date: 2025/6/7 1:35
 */
@Data
public class FunctionTreeSyncAuditJournalVO implements Serializable {

    private Long id;
    /**
     * 任务流水号
     */
    private String taskSerial;

    /**
     * 审核人id
     */
    private Long auditorId;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核成功/失败
     */
    private Boolean auditResult;

    /**
     * 审核结果描述
     */
    private String auditMessage;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date updateTime;

}
