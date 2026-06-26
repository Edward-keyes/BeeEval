package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xailab.vehicle.xaivehicledata.dao.QaCacheDao;
import com.xailab.vehicle.xaivehicledata.entity.QaCacheEntity;
import com.xailab.vehicle.xaivehicledata.entity.response.QaQueryResponse;
import com.xailab.vehicle.xaivehicledata.service.QaCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务实现
 * 支持多级缓存：L1本地缓存 + L2 Redis缓存 + L3数据库缓存
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaCacheServiceImpl implements QaCacheService {

    private final QaCacheDao qaCacheDao;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    // L1本地缓存
    private Cache<String, QaQueryResponse> localCache;

    // 缓存配置
    private static final String CACHE_PREFIX = "qa:query:";
    private static final long L1_CACHE_SIZE = 1000;
    private static final long L1_EXPIRE_MINUTES = 30;
    private static final long L2_EXPIRE_SECONDS = 1800; // 30分钟
    private static final long L3_EXPIRE_HOURS = 24;

    @PostConstruct
    public void init() {
        // 初始化本地缓存
        localCache = Caffeine.newBuilder()
                .maximumSize(L1_CACHE_SIZE)
                .expireAfterWrite(L1_EXPIRE_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .removalListener((key, value, cause) -> {
                    log.debug("本地缓存移除: key={}, cause={}", key, cause);
                })
                .build();

        log.info("智能问答缓存服务已初始化");
    }

    @Override
    public QaQueryResponse getCache(String cacheKey) {
        // 1. 尝试从L1本地缓存获取
        QaQueryResponse response = localCache.getIfPresent(cacheKey);
        if (response != null) {
            log.debug("L1缓存命中: {}", cacheKey);
            return response;
        }

        // 2. 尝试从L2 Redis缓存获取
        if (redisTemplate != null) {
            String redisKey = CACHE_PREFIX + cacheKey;
            Object redisValue = redisTemplate.opsForValue().get(redisKey);
            if (redisValue instanceof QaQueryResponse) {
                response = (QaQueryResponse) redisValue;
                // 同步到L1缓存
                localCache.put(cacheKey, response);
                log.debug("L2缓存命中: {}", cacheKey);
                return response;
            }
        }

        // 3. 尝试从L3数据库缓存获取
        QaCacheEntity dbCache = qaCacheDao.selectByCacheKey(cacheKey);
        if (dbCache != null && dbCache.getExpireTime().isAfter(LocalDateTime.now())) {
            response = JSON.parseObject(dbCache.getAnswer(), QaQueryResponse.class);
            if (response != null) {
                // 同步到L1和L2缓存
                localCache.put(cacheKey, response);
                if (redisTemplate != null) {
                    String redisKey = CACHE_PREFIX + cacheKey;
                    redisTemplate.opsForValue().set(redisKey, response, L2_EXPIRE_SECONDS, TimeUnit.SECONDS);
                }

                // 更新命中次数
                qaCacheDao.incrementHitCount(dbCache.getId());

                log.debug("L3缓存命中: {}", cacheKey);
                return response;
            }
        }

        log.debug("缓存未命中: {}", cacheKey);
        return null;
    }

    @Override
    public void setCache(String cacheKey, QaQueryResponse response, long timeoutSeconds) {
        if (response == null || cacheKey == null) {
            return;
        }

        try {
            // 1. 设置L1本地缓存
            localCache.put(cacheKey, response);

            // 2. 设置L2 Redis缓存
            if (redisTemplate != null) {
                String redisKey = CACHE_PREFIX + cacheKey;
                redisTemplate.opsForValue().set(redisKey, response, timeoutSeconds, TimeUnit.SECONDS);
            }

            // 3. 设置L3数据库缓存（仅对重要查询）
            if (isImportantQuery(response)) {
                saveToDatabaseCache(cacheKey, response, timeoutSeconds);
            }

            log.debug("缓存设置成功: {}", cacheKey);
        } catch (Exception e) {
            log.warn("设置缓存失败: {}, 错误: {}", cacheKey, e.getMessage());
        }
    }

    @Override
    public void deleteCache(String cacheKey) {
        try {
            // 1. 删除L1本地缓存
            localCache.invalidate(cacheKey);

            // 2. 删除L2 Redis缓存
            if (redisTemplate != null) {
                String redisKey = CACHE_PREFIX + cacheKey;
                redisTemplate.delete(redisKey);
            }

            // 3. 删除L3数据库缓存
            qaCacheDao.deleteById(cacheKey);

            log.debug("缓存删除成功: {}", cacheKey);
        } catch (Exception e) {
            log.warn("删除缓存失败: {}, 错误: {}", cacheKey, e.getMessage());
        }
    }

    @Override
    public void cleanupExpiredCache() {
        try {
            // 清理L3数据库过期缓存
            int deleted = qaCacheDao.deleteExpiredCache();
            if (deleted > 0) {
                log.info("清理了{}条过期的数据库缓存", deleted);
            }

            // 清理L2 Redis缓存（通过过期时间自动清理）
            // Redis会自动清理过期键

            // 清理L1本地缓存（通过Caffeine自动清理）
        } catch (Exception e) {
            log.warn("清理过期缓存失败: {}", e.getMessage());
        }
    }

    @Override
    public String generateCacheKey(String question, String userId, Object params) {
        StringBuilder keyBuilder = new StringBuilder();

        // 添加用户ID（不同用户可能有不同的权限）
        if (userId != null) {
            keyBuilder.append(userId).append(":");
        }

        // 添加问题内容（标准化处理）
        String normalizedQuestion = normalizeQuestion(question);
        keyBuilder.append(normalizedQuestion);

        // 添加参数
        if (params != null) {
            String paramStr = params.toString();
            if (!paramStr.isEmpty()) {
                keyBuilder.append(":").append(paramStr.hashCode());
            }
        }

        String cacheKey = keyBuilder.toString();
        return Integer.toString(cacheKey.hashCode());
    }

    @Override
    public CacheStats getCacheStats() {
        // 获取L3数据库缓存统计
        QaCacheEntity stats = qaCacheDao.selectCacheStats();

        long totalCount = stats != null ? stats.getId().longValue() : 0;
        long validCount = 0;
        long totalHits = 0;

        if (stats != null) {
            // 这里简化处理，实际应该从统计数据中获取
            validCount = totalCount;
            totalHits = stats.getHitCount() != null ? stats.getHitCount().longValue() : 0;
        }

        return new CacheStats(totalCount, validCount, totalHits);
    }

    /**
     * 保存到数据库缓存
     */
    private void saveToDatabaseCache(String cacheKey, QaQueryResponse response, long timeoutSeconds) {
        try {
            QaCacheEntity cacheEntity = new QaCacheEntity();
            cacheEntity.setCacheKey(cacheKey);
            cacheEntity.setQuestionHash(generateQuestionHash(response.getQueryId()));
            cacheEntity.setQuestion(""); // 简化存储
            cacheEntity.setAnswer(JSON.toJSONString(response));
            cacheEntity.setSqlResult(""); // 简化存储
            cacheEntity.setVisualizationData(""); // 简化存储
            cacheEntity.setExpireTime(LocalDateTime.now().plusSeconds(timeoutSeconds));
            cacheEntity.setHitCount(0);
            cacheEntity.setCreateTime(LocalDateTime.now());

            qaCacheDao.insert(cacheEntity);
        } catch (Exception e) {
            log.warn("保存数据库缓存失败: {}", e.getMessage());
        }
    }

    /**
     * 判断是否为重要查询
     */
    private boolean isImportantQuery(QaQueryResponse response) {
        // 成功率高的查询
        if (response.getConfidenceScore() != null && response.getConfidenceScore() > 0.8) {
            return true;
        }

        // 复杂查询
        if (response.getQueryInfo() != null &&
                response.getQueryInfo().getSqlGenerated() != null &&
                response.getQueryInfo().getSqlGenerated().length() > 100) {
            return true;
        }

        // 有可视化的查询
        if (response.getVisualization() != null) {
            return true;
        }

        return false;
    }

    /**
     * 标准化问题内容
     */
    private String normalizeQuestion(String question) {
        if (question == null) {
            return "";
        }

        return question.trim()
                .toLowerCase()
                .replaceAll("\\s+", " ") // 多个空格替换为单个空格
                .replaceAll("[\\p{Punct}&&[^，。？！；：\"'（）【】《》]]", ""); // 保留中文标点
    }

    /**
     * 生成问题哈希
     */
    private String generateQuestionHash(String queryId) {
        return queryId != null ? Integer.toString(queryId.hashCode()) : "";
    }

    /**
     * 预热热门缓存
     */
    public void preheatPopularCache() {
        try {
            List<QaCacheEntity> popularCaches = qaCacheDao.selectPopularCache(50);
            for (QaCacheEntity cache : popularCaches) {
                if (cache.getExpireTime().isAfter(LocalDateTime.now())) {
                    QaQueryResponse response = JSON.parseObject(cache.getAnswer(), QaQueryResponse.class);
                    if (response != null) {
                        // 预热到L1和L2缓存
                        localCache.put(cache.getCacheKey(), response);
                        if (redisTemplate != null) {
                            String redisKey = CACHE_PREFIX + cache.getCacheKey();
                            redisTemplate.opsForValue().set(redisKey, response, L2_EXPIRE_SECONDS, TimeUnit.SECONDS);
                        }
                    }
                }
            }
            log.info("缓存预热完成，加载了{}条热门缓存", popularCaches.size());
        } catch (Exception e) {
            log.warn("缓存预热失败: {}", e.getMessage());
        }
    }
}
