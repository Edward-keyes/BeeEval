package com.xailab.vehicle.xaivehicledata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 向量嵌入配置
 */
@Configuration
@ConfigurationProperties(prefix = "qa.embedding")
@Data
public class EmbeddingConfig {

    /**
     * API密钥（默认使用通义千问API Key）
     */
    private String apiKey;

    /**
     * 嵌入模型
     * text-embedding-v1: 通义千问文本嵌入模型，维度1536
     * text-embedding-v2: 通义千问文本嵌入模型v2，维度1536
     */
    private String model = "text-embedding-v2";

    /**
     * 向量维度
     */
    private int dimension = 1536;

    /**
     * 批量处理大小
     */
    private int batchSize = 20;

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    /**
     * 重试间隔(毫秒)
     */
    private long retryIntervalMs = 1000;

    /**
     * 请求超时时间(秒)
     */
    private int requestTimeoutSeconds = 60;

    /**
     * 是否启用缓存
     */
    private boolean enableCache = true;

    /**
     * 缓存过期时间(小时)
     */
    private int cacheExpireHours = 24;

    /**
     * 相似度阈值
     */
    private double similarityThreshold = 0.7;

    /**
     * 检索返回数量
     */
    private int topK = 10;
}
