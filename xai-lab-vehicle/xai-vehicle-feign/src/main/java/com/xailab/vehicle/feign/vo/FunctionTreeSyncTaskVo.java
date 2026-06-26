package com.xailab.vehicle.feign.vo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class FunctionTreeSyncTaskVo {

    private Long id;

    /**
     * 任务流水号
     * 随机
     */
    private String taskSerial;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务描述
     */
    private String description;

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
     * 同步规则
     * 0: 默认结构关联
     * 1: 功能id关联
     * 2: 自定义关联
     */
    private Integer syncRule;

    /**
     * 任务状态
     * 0: 审核中
     * 1: 审核通过
     * 2: 审核失败
     * 3: 同步成功
     * 4: 同步失败
     */
    private Integer taskState;

    /**
     * 任务审核失败原因
     */
    private String taskMessage;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除 0/1 否/是
     */
    private Integer deleted;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 更新者
     */
    private Long updater;

}
