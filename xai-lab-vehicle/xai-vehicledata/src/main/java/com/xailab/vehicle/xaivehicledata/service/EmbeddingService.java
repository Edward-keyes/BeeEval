package com.xailab.vehicle.xaivehicledata.service;

import java.util.List;

/**
 * 文本向量嵌入服务
 */
public interface EmbeddingService {

    /**
     * 将文本转换为向量
     *
     * @param text 输入文本
     * @return 向量数组
     */
    float[] embed(String text);

    /**
     * 批量将文本转换为向量
     *
     * @param texts 输入文本列表
     * @return 向量数组列表
     */
    List<float[]> batchEmbed(List<String> texts);

    /**
     * 计算两个向量的余弦相似度
     *
     * @param vector1 向量1
     * @param vector2 向量2
     * @return 相似度 [0, 1]
     */
    double cosineSimilarity(float[] vector1, float[] vector2);

    /**
     * 计算两个文本的相似度
     *
     * @param text1 文本1
     * @param text2 文本2
     * @return 相似度 [0, 1]
     */
    double textSimilarity(String text1, String text2);

    /**
     * 健康检查
     *
     * @return 是否健康
     */
    boolean isHealthy();

    /**
     * 获取向量维度
     *
     * @return 向量维度
     */
    int getDimension();

    /**
     * 获取统计信息
     *
     * @return 统计信息
     */
    EmbeddingStats getStats();

    /**
     * 嵌入统计信息
     */
    record EmbeddingStats(
            long totalCalls,
            long successfulCalls,
            long failedCalls,
            double averageResponseTime,
            long lastCallTime,
            long cacheHits,
            long cacheMisses) {
    }
}
