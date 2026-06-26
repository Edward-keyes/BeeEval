package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 问题模板请求DTO
 */
@Data
public class QaTemplateRequest {

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    private String name;

    /**
     * 模板描述
     */
    private String description;

    /**
     * 分类
     */
    @NotBlank(message = "分类不能为空")
    private String category;

    /**
     * 问题匹配模式(正则)
     */
    private String questionPattern;

    /**
     * 关键词列表（JSON字符串）
     */
    private String keywords;

    /**
     * SQL模板
     */
    @NotBlank(message = "SQL模板不能为空")
    private String sqlTemplate;

    /**
     * 参数配置（JSON字符串）
     */
    private String parameters;

    /**
     * 默认可视化类型
     */
    private String visualizationType;

    /**
     * 可视化配置（JSON字符串）
     */
    private String visualizationConfig;

    /**
     * 优先级
     */
    private Integer priority = 0;

    /**
     * 创建人
     */
    private String createUser;
}
