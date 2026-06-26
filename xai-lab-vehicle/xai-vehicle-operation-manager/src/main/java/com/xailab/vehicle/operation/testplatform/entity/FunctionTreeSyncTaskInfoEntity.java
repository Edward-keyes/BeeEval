package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: FunctionTreeSyncTaskInfoEntity
 * @Description: 功能树同步任务详情表
 * @author: liulin
 * @date: 2025/6/2 18:31
 */
@Data
@TableName("function_tree_sync_task_info")
public class FunctionTreeSyncTaskInfoEntity {

    @TableId
    private Long id;

    /**
     * 关联同步任务流水号
     */
    private String taskSerial;


    /**
     * 功能id
     */
    private String functionTag;

    /**
     * 当且同步任务规则 为自定义关联 时需指定
     * 目标功能id
     */
    private String targetFunctionTag;


    /**
     * 使用List进行拼接 逗号分隔
     * id1,id2,id3
     * 测试用例ids
     */
    private String testCaseId;

    /**
     * 同步选项数据
     * 通过-拼接
     * caseid1_题1选1,题1选2-题2选1,题2选2;caseid2...
     */
    private String syncOption;

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
