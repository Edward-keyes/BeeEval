package com.xailab.vehicle.operation.testplatform.enums;

import com.xailab.vehicle.framework.common.exception.ServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: FunctionTreeTaskOperationEunm
 * @Description:
 * @author: liulin
 * @date: 2025/7/7 1:16
 */
@AllArgsConstructor
@Getter
public enum FunctionTreeTaskOperationEnum {
    /**
     * 同步任务操作枚举
     * 0 - 创建
     * 1 - 修改
     * 2 - 预览同步
     * 3 - 审核
     */
    CREATE_SYNC_TASK(0,"create" ,"创建同步任务"),
    MODIFY_SYNC_TASK(1,"update" ,"修改同步任务"),
    PREVIEW_SYNC_TASK(2,"sync", "同步镜像"),
    //提交审核
    SUBMIT_AUDIT_SYNC_TASK(3,"submitAudit", "提交审核"),
    AUDIT_SYNC_TASK(4,"audit", "审核同步任务"),
    DELETE_SYNC_TASK(5,"delete", "删除同步任务"),

    //后台初始化
    INIT_SYNC_TASK(6,"init", "初始化同步任务数据"),

    ;

    private final Integer code;
    private final String name;
    private final String description;
    /**
     * 根据状态码获取枚举实例
     * @param code 状态码
     * @return 对应的枚举实例
     * @throws IllegalArgumentException 如果找不到对应的枚举值
     */
    public static FunctionTreeTaskStatusEnum fromCode(int code) {
        for (FunctionTreeTaskStatusEnum status : FunctionTreeTaskStatusEnum.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new ServerException("无效的任务状态码: " + code);
    }

    public boolean equals(Integer code) {
        return this.code.equals(code);
    }
}
