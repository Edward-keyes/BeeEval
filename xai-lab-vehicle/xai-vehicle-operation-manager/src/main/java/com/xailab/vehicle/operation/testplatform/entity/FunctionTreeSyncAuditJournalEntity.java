package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: FunctionTreeSyncAuditJournalEntity
 * @Description: 审核记录表
 * @author: liulin
 * @date: 2025/6/3 23:07
 */
@Data
@TableName("function_tree_sync_journal_entity")
public class FunctionTreeSyncAuditJournalEntity implements Serializable {

    @TableId
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

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
