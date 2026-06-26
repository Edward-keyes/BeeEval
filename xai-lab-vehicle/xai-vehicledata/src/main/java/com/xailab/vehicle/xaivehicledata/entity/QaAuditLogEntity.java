package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询审计表实体类
 */
@Data
@TableName("qa_audit_log")
public class QaAuditLogEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 查询ID
     */
    private Long queryId;

    /**
     * 问题内容
     */
    private String question;

    /**
     * 执行的SQL
     */
    private String sqlExecuted;

    /**
     * 结果条数
     */
    private Integer resultCount;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 设备信息
     */
    private String deviceInfo;

    /**
     * 访问时间
     */
    private LocalDateTime accessTime;

    /**
     * 响应时间(毫秒)
     */
    private Long responseTime;

    /**
     * 是否涉及敏感数据
     */
    private Boolean isSensitive;

    /**
     * 风险等级 low/medium/high
     */
    private String riskLevel;
}
