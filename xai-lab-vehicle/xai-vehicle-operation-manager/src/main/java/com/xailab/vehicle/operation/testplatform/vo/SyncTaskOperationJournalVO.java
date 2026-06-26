package com.xailab.vehicle.operation.testplatform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SyncTaskOperationJournalVO
 * @Description:
 * @author: liulin
 * @date: 2025/7/9 23:36
 */
@Data
public class SyncTaskOperationJournalVO implements Serializable {

    private Long id;

    /**
     * 同步任务流水号
     */
    private String taskSerial;

    /**
     * 操作类型
     */
    private String operationType;


    /**
     * 操作结果
     */
    private Boolean operationResult;

    /**
     * 操作信息
     */
    private String operationMessage;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date operationTime;

    /**
     * 操作人
     */
    private String operationUser;

}
