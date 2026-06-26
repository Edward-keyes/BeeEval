package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问题模板VO
 */
@Data
public class QaTemplateVO {

    /**
     * 模板ID
     */
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板描述
     */
    private String description;

    /**
     * 分类
     */
    private String category;

    /**
     * 默认可视化类型
     */
    private String visualizationType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 使用次数
     */
    private Integer hitCount;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
