package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询历史表实体类
 */
@Data
@TableName("qa_query_history")
public class QaQueryHistoryEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户问题
     */
    private String question;

    /**
     * 问题向量表示
     */
    private String questionEmbedding;

    /**
     * 生成的SQL语句
     */
    private String generatedSql;

    /**
     * SQL查询结果
     */
    private String sqlResult;

    /**
     * 生成的答案
     */
    private String answer;

    /**
     * 答案向量表示
     */
    private String answerEmbedding;

    /**
     * 使用的模板ID
     */
    private Long templateId;

    /**
     * 可视化类型
     */
    private String visualizationType;

    /**
     * 可视化配置
     */
    private String visualizationConfig;

    /**
     * 查询状态 success/failed/processing
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 响应时间(毫秒)
     */
    private Integer responseTime;

    /**
     * 是否缓存命中
     */
    private Boolean isCacheHit;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
