package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.response.QaQueryResponse;

/**
 * 缓存管理服务接口
 */
public interface QaCacheService {

    /**
     * 获取缓存
     */
    QaQueryResponse getCache(String cacheKey);

    /**
     * 设置缓存
     */
    void setCache(String cacheKey, QaQueryResponse response, long timeoutSeconds);

    /**
     * 删除缓存
     */
    void deleteCache(String cacheKey);

    /**
     * 清理过期缓存
     */
    void cleanupExpiredCache();

    /**
     * 生成缓存键
     */
    String generateCacheKey(String question, String userId, Object params);

    /**
     * 获取缓存统计
     */
    CacheStats getCacheStats();

    /**
     * 类定义
     */
    class CacheStats {
        private long totalCount;
        private long validCount;
        private long totalHits;

        public CacheStats(long totalCount, long validCount, long totalHits) {
            this.totalCount = totalCount;
            this.validCount = validCount;
            this.totalHits = totalHits;
        }

        // Getters
        public long getTotalCount() {
            return totalCount;
        }

        public long getValidCount() {
            return validCount;
        }

        public long getTotalHits() {
            return totalHits;
        }
    }
}
