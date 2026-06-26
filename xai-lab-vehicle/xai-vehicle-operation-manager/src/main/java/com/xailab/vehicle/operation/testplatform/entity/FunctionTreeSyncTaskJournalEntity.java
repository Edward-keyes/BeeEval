package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: FunctionTreeSyncTaskJournalEntity
 * @Description:
 * @author: liulin
 * @date: 2025/7/6 18:14
 */
@Data
@TableName("function_tree_sync_task_journal")
public class FunctionTreeSyncTaskJournalEntity implements Serializable {

    @TableId
    private Long id;

    /**
     * 任务流水号
     * 随机
     */
    private String taskSerial;

    /**
     * 测试任务id
     * 原始车辆
     */
    private String testRecordId;

    /**
     * 车辆id
     * 目标车辆
     * beeeval 平台对应车辆id
     */
    private String vehicleId;

    /**
     * 同步环境
     */
    private String environment;

    /**
     * 操作记录id
     */
    private Long operationId;


    /**
     * 同步信息 json数据
     */
    private String syncInfoJournal;


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
