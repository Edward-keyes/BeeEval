package com.xailab.vehicle.operation.testplatform.enums;

import com.xailab.vehicle.framework.common.exception.ServerException;
import lombok.Getter;

/**
 * 任务状态枚举
 */
@Getter
public enum FunctionTreeTaskStatusEnum {
    /**
     * 审核中
     */
    UNDER_REVIEW(0, "审核中"),
    
    /**
     * 审核通过
     */
    APPROVED(1, "审核通过"),
    
    /**
     * 审核失败
     */
    REJECTED(2, "审核失败"),

    /**
     * 等待同步预览
     */
    SYNC_PREVIEW(3, "等待同步镜像"),

    /**
     * 同步镜像成功
     */
    SYNC_PREVIEW_SUCCESS(4, "同步镜像成功"),

    /**
     * 同步镜像失败
     */
    SYNC_PREVIEW_FAILED(5, "同步镜像失败"),


    ;
    private final Integer code;
    private final String description;

    /**
     * 构造函数
     * @param code 状态码
     * @param description 状态描述
     */
    FunctionTreeTaskStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
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