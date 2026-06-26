package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.xaivehicledata.dao.QaAuditLogDao;
import com.xailab.vehicle.xaivehicledata.entity.QaAuditLogEntity;
import com.xailab.vehicle.xaivehicledata.service.QaAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审计日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaAuditServiceImpl implements QaAuditService {

    private final QaAuditLogDao qaAuditLogDao;

    @Override
    @Transactional
    public void logQueryAudit(String userId, String userName, String sessionId,
            Long queryId, String question, String sqlExecuted,
            Integer resultCount, String ipAddress,
            String userAgent, Long responseTime,
            Boolean isSensitive, String riskLevel) {

        try {
            QaAuditLogEntity auditLog = new QaAuditLogEntity();
            auditLog.setUserId(userId);
            auditLog.setUserName(userName);
            auditLog.setSessionId(sessionId);
            auditLog.setQueryId(queryId);
            auditLog.setQuestion(question);
            auditLog.setSqlExecuted(sqlExecuted);
            auditLog.setResultCount(resultCount);
            auditLog.setIpAddress(ipAddress);
            auditLog.setUserAgent(userAgent);
            auditLog.setAccessTime(LocalDateTime.now());
            auditLog.setResponseTime(responseTime);
            auditLog.setIsSensitive(isSensitive != null ? isSensitive : false);
            auditLog.setRiskLevel(riskLevel != null ? riskLevel : "low");

            qaAuditLogDao.insert(auditLog);

            log.debug("审计日志记录成功: userId={}, queryId={}", userId, queryId);
        } catch (Exception e) {
            log.warn("审计日志记录失败: userId={}, error={}", userId, e.getMessage());
            // 不抛出异常，避免影响正常业务流程
        }
    }

    @Override
    public List<QaAuditLogEntity> getUserAuditLogs(String userId, Integer page, Integer size) {
        if (page == null || page < 1)
            page = 1;
        if (size == null || size < 1)
            size = 20;

        int offset = (page - 1) * size;

        return qaAuditLogDao.selectList(
                new QueryWrapper<QaAuditLogEntity>()
                        .eq("user_id", userId)
                        .orderByDesc("access_time")
                        .last("LIMIT " + offset + ", " + size));
    }

    @Override
    public List<QaAuditLogEntity> getAuditLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return qaAuditLogDao.selectByTimeRange(startTime, endTime);
    }

    @Override
    public List<QaAuditLogEntity> getSensitiveAccessLogs() {
        return qaAuditLogDao.selectSensitiveAccess();
    }

    @Override
    public boolean checkUserAccessFrequency(String userId) {
        // 检查用户在最近1分钟内的访问次数
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        Long accessCount = qaAuditLogDao.countUserAccess(userId, oneMinuteAgo);

        // 限制每分钟最多10次查询
        return accessCount < 10;
    }

    @Override
    public boolean checkIpAccessFrequency(String ipAddress) {
        // 检查IP在最近1分钟内的访问次数
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        Long accessCount = qaAuditLogDao.countIpAccess(ipAddress, oneMinuteAgo);

        // 限制每分钟最多50次查询（来自同一IP）
        return accessCount < 50;
    }

    @Override
    public AuditStats getAuditStats(LocalDateTime startTime, LocalDateTime endTime) {
        // 这里简化实现，实际应该通过统计查询获取
        List<QaAuditLogEntity> logs = qaAuditLogDao.selectByTimeRange(startTime, endTime);

        long totalQueries = logs.size();
        long sensitiveQueries = logs.stream().mapToLong(log -> log.getIsSensitive() ? 1 : 0).sum();
        long highRiskQueries = logs.stream().mapToLong(log -> "high".equals(log.getRiskLevel()) ? 1 : 0).sum();
        long uniqueUsers = logs.stream().map(QaAuditLogEntity::getUserId).distinct().count();
        long uniqueIPs = logs.stream().map(QaAuditLogEntity::getIpAddress).distinct().count();

        return new AuditStats(totalQueries, sensitiveQueries, highRiskQueries, uniqueUsers, uniqueIPs);
    }
}
