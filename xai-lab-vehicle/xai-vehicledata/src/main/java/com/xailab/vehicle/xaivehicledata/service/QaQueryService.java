package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.request.QaQueryRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.QaQueryResponse;

/**
 * 问答查询服务接口
 */
public interface QaQueryService {

    /**
     * 处理用户查询
     */
    QaQueryResponse processQuery(QaQueryRequest request);

    /**
     * 重新生成答案
     */
    QaQueryResponse regenerateAnswer(String queryId);

    /**
     * 获取查询详情
     */
    QaQueryResponse getQueryDetail(String queryId);

    /**
     * 验证SQL安全性
     */
    boolean validateSql(String sql);

    /**
     * 执行SQL查询
     */
    Object executeSql(String sql);
}
