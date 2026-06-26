package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.xaivehicledata.dao.QaQueryHistoryDao;
import com.xailab.vehicle.xaivehicledata.dao.QaSessionDao;
import com.xailab.vehicle.xaivehicledata.entity.QaQueryHistoryEntity;
import com.xailab.vehicle.xaivehicledata.entity.QaSessionEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.QaSessionRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.QaSessionResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.QaSessionHistoryVO;
import com.xailab.vehicle.xaivehicledata.service.QaSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 问答会话服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaSessionServiceImpl implements QaSessionService {

    private final QaSessionDao qaSessionDao;
    private final QaQueryHistoryDao qaQueryHistoryDao;

    @Override
    @Transactional
    public QaSessionResponse createSession(QaSessionRequest request) {
        // 生成唯一会话ID
        String sessionId = "sess-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        QaSessionEntity session = new QaSessionEntity();
        session.setSessionId(sessionId);
        session.setUserId(request.getUserId());
        session.setUserName(request.getUserName());
        session.setStatus("active");
        session.setLastActivityTime(LocalDateTime.now());
        session.setCreateTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());

        qaSessionDao.insert(session);

        log.info("Created new QA session: {} for user: {}", sessionId, request.getUserId());

        return QaSessionResponse.builder()
                .sessionId(sessionId)
                .status("active")
                .createTime(session.getCreateTime())
                .lastActivityTime(session.getLastActivityTime())
                .build();
    }

    @Override
    public QaSessionResponse getSession(String sessionId) {
        QaSessionEntity session = qaSessionDao.selectOne(
            new QueryWrapper<QaSessionEntity>()
                .eq("session_id", sessionId)
                .eq("status", "active")
        );

        if (session == null) {
            return null;
        }

        return QaSessionResponse.builder()
                .sessionId(session.getSessionId())
                .status(session.getStatus())
                .createTime(session.getCreateTime())
                .lastActivityTime(session.getLastActivityTime())
                .build();
    }

    @Override
    @Transactional
    public void updateSessionActivity(String sessionId) {
        QaSessionEntity session = new QaSessionEntity();
        session.setLastActivityTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());

        qaSessionDao.update(session,
            new QueryWrapper<QaSessionEntity>()
                .eq("session_id", sessionId)
        );
    }

    @Override
    public List<QaSessionHistoryVO> getSessionHistory(String sessionId, Integer page, Integer size) {
        // 默认分页参数
        if (page == null || page < 1) page = 1;
        if (size == null || size < 1) size = 20;

        int offset = (page - 1) * size;

        List<QaQueryHistoryEntity> histories = qaQueryHistoryDao.selectBySessionId(sessionId);

        return histories.stream()
                .skip(offset)
                .limit(size)
                .map(this::convertToHistoryVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSession(String sessionId) {
        // 软删除会话
        QaSessionEntity session = new QaSessionEntity();
        session.setStatus("inactive");
        session.setUpdateTime(LocalDateTime.now());

        qaSessionDao.update(session,
            new QueryWrapper<QaSessionEntity>()
                .eq("session_id", sessionId)
        );

        log.info("Deleted QA session: {}", sessionId);
    }

    @Override
    @Transactional
    public void cleanupExpiredSessions() {
        // 清理7天前的不活跃会话
        LocalDateTime expireTime = LocalDateTime.now().minusDays(7);

        QaSessionEntity session = new QaSessionEntity();
        session.setStatus("expired");
        session.setUpdateTime(LocalDateTime.now());

        int updated = qaSessionDao.update(session,
            new QueryWrapper<QaSessionEntity>()
                .eq("status", "inactive")
                .lt("last_activity_time", expireTime)
        );

        if (updated > 0) {
            log.info("Cleaned up {} expired QA sessions", updated);
        }
    }

    private QaSessionHistoryVO convertToHistoryVO(QaQueryHistoryEntity entity) {
        QaSessionHistoryVO vo = new QaSessionHistoryVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setQueryId(entity.getId());
        return vo;
    }
}
