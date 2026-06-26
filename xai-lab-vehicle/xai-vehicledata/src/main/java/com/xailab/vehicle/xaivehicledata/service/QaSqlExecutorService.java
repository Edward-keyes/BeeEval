package com.xailab.vehicle.xaivehicledata.service;

import java.util.List;
import java.util.Map;

/**
 * SQL执行服务接口
 * 用于执行动态SQL查询
 */
public interface QaSqlExecutorService {

    /**
     * 执行SQL查询
     * 
     * @param sql SQL语句
     * @return 查询结果列表
     */
    List<Map<String, Object>> executeQuery(String sql);

    /**
     * 执行SQL查询（带参数）
     * 
     * @param sql    SQL语句
     * @param params 参数映射
     * @return 查询结果列表
     */
    List<Map<String, Object>> executeQuery(String sql, Map<String, Object> params);

    /**
     * 验证SQL是否安全
     * 
     * @param sql SQL语句
     * @return 是否安全
     */
    boolean validateSql(String sql);

    /**
     * 获取SQL的表名
     * 
     * @param sql SQL语句
     * @return 表名列表
     */
    List<String> extractTableNames(String sql);
}
