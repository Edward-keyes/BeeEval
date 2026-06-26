package com.xailab.vehicle.operation.testplatform.enums;

import lombok.Getter;

/**
 * 同步规则枚举
 */
@Getter
public enum FunctionTreeTaskSyncRuleEnum {
    /**
     * 默认结构关联
     */
    DEFAULT_LINK(0, "默认结构关联"),

    /**
     * 功能id关联
     */
    FUNCTION_ID_LINK(1, "功能id关联"),

    /**
     * 自定义关联
     */
    CUSTOM_LINK(2, "自定义关联");

    private final Integer code;
    private final String description;

    /**
     * 构造函数
     * @param code 规则编码
     * @param description 规则描述
     */
    FunctionTreeTaskSyncRuleEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举实例
     * @param code 规则编码
     * @return 对应的枚举实例
     * @throws IllegalArgumentException 如果编码无效
     */
    public static FunctionTreeTaskSyncRuleEnum fromCode(int code) {
        for (FunctionTreeTaskSyncRuleEnum rule : FunctionTreeTaskSyncRuleEnum.values()) {
            if (rule.getCode() == code) {
                return rule;
            }
        }
        throw new IllegalArgumentException("无效的同步规则编码: " + code);
    }
    public boolean equals(Integer code) {
        return this.code.equals(code);
    }
}