package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.QaAuditLogEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审计日志服务接口
 */
public interface QaAuditService {

    /**
     * 记录查询审计日志
     */
    void logQueryAudit(String userId, String userName, String sessionId,
                      Long queryId, String question, String sqlExecuted,
                      Integer resultCount, String ipAddress,
                      String userAgent, Long responseTime,
                      Boolean isSensitive, String riskLevel);

    /**
     * 获取用户审计日志
     */
    List<QaAuditLogEntity> getUserAuditLogs(String userId, Integer page, Integer size);

    /**
     * 获取时间范围内的审计日志
     */
    List<QaAuditLogEntity> getAuditLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取敏感数据访问记录
     */
    List<QaAuditLogEntity> getSensitiveAccessLogs();

    /**
     * 检查用户访问频率
     */
    boolean checkUserAccessFrequency(String userId);

    /**
     * 检查IP访问频率
     */
    boolean checkIpAccessFrequency(String ipAddress);

    /**
     * 获取审计统计信息
     */
    AuditStats getAuditStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 审计统计类
     */
    class AuditStats {
        private long totalQueries;
        private long sensitiveQueries;
        private long highRiskQueries;
        private long uniqueUsers;
        private long uniqueIPs;

        public AuditStats(long totalQueries, long sensitiveQueries, long highRiskQueries,
                         long uniqueUsers, long uniqueIPs) {
            this.totalQueries = totalQueries;
            this.sensitiveQueries = sensitiveQueries;
            this.highRiskQueries = highRiskQueries;
            this.uniqueUsers = uniqueUsers;
            this.uniqueIPs = uniqueIPs;
        }

        // Getters
        public long getTotalQueries() { return totalQueries; }
        public long getSensitiveQueries() { return sensitiveQueries; }
        public long getHighRiskQueries() { return highRiskQueries; }
        public long getUniqueUsers() { return uniqueUsers; }
        public long getUniqueIPs() { return uniqueIPs; }
    }
}
