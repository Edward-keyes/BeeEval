package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.request.QaSessionRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.QaSessionResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.QaSessionHistoryVO;

import java.util.List;

/**
 * 问答会话服务接口
 */
public interface QaSessionService {

    /**
     * 创建新会话
     */
    QaSessionResponse createSession(QaSessionRequest request);

    /**
     * 获取会话信息
     */
    QaSessionResponse getSession(String sessionId);

    /**
     * 更新会话活动时间
     */
    void updateSessionActivity(String sessionId);

    /**
     * 获取会话历史
     */
    List<QaSessionHistoryVO> getSessionHistory(String sessionId, Integer page, Integer size);

    /**
     * 删除会话
     */
    void deleteSession(String sessionId);

    /**
     * 清理过期会话
     */
    void cleanupExpiredSessions();
}
