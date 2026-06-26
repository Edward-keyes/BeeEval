package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 缓存表实体类
 */
@Data
@TableName("qa_cache")
public class QaCacheEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 缓存键(MD5)
     */
    private String cacheKey;

    /**
     * 问题哈希
     */
    private String questionHash;

    /**
     * 原始问题
     */
    private String question;

    /**
     * 答案内容
     */
    private String answer;

    /**
     * SQL结果
     */
    private String sqlResult;

    /**
     * 可视化数据
     */
    private String visualizationData;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 命中次数
     */
    private Integer hitCount;

    /**
     * 最后访问时间
     */
    private LocalDateTime lastAccessTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
