package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 问题模板表实体类
 */
@Data
@TableName("qa_question_template")
public class QaQuestionTemplateEntity {

    @TableId(type = IdType.AUTO)
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
     * 问题匹配模式(正则)
     */
    private String questionPattern;

    /**
     * 关键词列表
     */
    private String keywords;

    /**
     * SQL模板
     */
    private String sqlTemplate;

    /**
     * 参数配置
     */
    private String parameters;

    /**
     * 默认可视化类型
     */
    private String visualizationType;

    /**
     * 可视化配置
     */
    private String visualizationConfig;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否启用
     */
    private Boolean isActive;

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
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
}
