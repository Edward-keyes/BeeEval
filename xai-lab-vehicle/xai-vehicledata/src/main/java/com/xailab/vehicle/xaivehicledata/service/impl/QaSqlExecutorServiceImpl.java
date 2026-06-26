package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaivehicledata.service.QaSqlExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL执行服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaSqlExecutorServiceImpl implements QaSqlExecutorService {

    private final JdbcTemplate jdbcTemplate;

    private static final List<String> ALLOWED_TABLES = Arrays.asList(
            "vehicle_brand",
            "vehicle_base_info",
            "vehicle_functional_domain",
            "vehicle_domain_index",
            "vehicle_domain_score",
            "beeeval_open_case_score",
            "beeeval_open_source_case",
            "vehicle_function_domain_video");

    private static final List<String> SQL_BLACKLIST = Arrays.asList(
            "drop", "delete", "update", "insert", "alter", "create", "truncate",
            "exec", "execute", "union", "select.*into", "load_file", "outfile",
            "grant", "revoke", "commit", "rollback");

    @Override
    public List<Map<String, Object>> executeQuery(String sql) {
        return executeQuery(sql, Map.of());
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql, Map<String, Object> params) {
        log.info("执行SQL查询: sql={}, params={}", sql, params);

        if (!validateSql(sql)) {
            throw new IllegalArgumentException("SQL包含不安全操作或访问了不允许的表");
        }

        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
            log.info("SQL查询成功，返回{}条记录", result.size());
            return result;
        } catch (Exception e) {
            log.error("SQL查询失败: sql={}, error={}", sql, e.getMessage(), e);
            throw new RuntimeException("SQL查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean validateSql(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return false;
        }

        String lowerSql = sql.toLowerCase();

        for (String keyword : SQL_BLACKLIST) {
            Pattern pattern = Pattern.compile("\\b" + keyword + "\\b");
            if (pattern.matcher(lowerSql).find()) {
                log.warn("SQL包含黑名单关键词: {}", keyword);
                return false;
            }
        }

        List<String> tableNames = extractTableNames(sql);
        for (String tableName : tableNames) {
            if (!ALLOWED_TABLES.contains(tableName)) {
                log.warn("SQL访问了不允许的表: {}", tableName);
                return false;
            }
        }

        if (!lowerSql.trim().startsWith("select")) {
            log.warn("SQL不是SELECT语句");
            return false;
        }

        return true;
    }

    @Override
    public List<String> extractTableNames(String sql) {
        List<String> tableNames = new ArrayList<>();

        Pattern pattern = Pattern.compile("(?:from|join)\\s+([a-zA-Z_][a-zA-Z0-9_]*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);

        while (matcher.find()) {
            String tableName = matcher.group(1).toLowerCase();
            if (!tableNames.contains(tableName)) {
                tableNames.add(tableName);
            }
        }

        log.debug("提取到的表名: {}", tableNames);
        return tableNames;
    }
}
