package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName: SyncTaskOperationJournalEntity
 * @Description:
 * @author: liulin
 * @date: 2025/7/7 23:47
 */
@Data
@TableName("function_tree_sync_operation_journal")
public class SyncTaskOperationJournalEntity implements Serializable {

    @TableId
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
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operationTime;

    /**
     * 操作人
     */
    private String operationUser;

}
