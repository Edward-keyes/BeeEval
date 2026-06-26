package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.dashscope.embeddings.TextEmbedding;
import com.alibaba.dashscope.embeddings.TextEmbeddingParam;
import com.alibaba.dashscope.embeddings.TextEmbeddingResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.xailab.vehicle.xaivehicledata.config.EmbeddingConfig;
import com.xailab.vehicle.xaivehicledata.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 通义千问文本向量嵌入服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingConfig embeddingConfig;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "embedding:";

    private volatile long totalCalls = 0;
    private volatile long successfulCalls = 0;
    private volatile long failedCalls = 0;
    private volatile double averageResponseTime = 0;
    private volatile long lastCallTime = 0;
    private volatile long cacheHits = 0;
    private volatile long cacheMisses = 0;

    @PostConstruct
    public void init() {
        if (embeddingConfig.getApiKey() != null && !embeddingConfig.getApiKey().isEmpty()) {
            log.info("通义千问Embedding API已初始化，模型: {}, 维度: {}",
                    embeddingConfig.getModel(), embeddingConfig.getDimension());
        } else {
            log.warn("通义千问Embedding API Key未配置，请在配置文件中设置 qa.embedding.api-key");
        }
    }

    @Override
    public float[] embed(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new float[embeddingConfig.getDimension()];
        }

        String cacheKey = generateCacheKey(text);

        if (embeddingConfig.isEnableCache()) {
            float[] cached = getFromCache(cacheKey);
            if (cached != null) {
                cacheHits++;
                log.debug("从缓存获取向量: text长度={}", text.length());
                return cached;
            }
            cacheMisses++;
        }

        long startTime = System.currentTimeMillis();

        try {
            totalCalls++;

            TextEmbeddingParam param = TextEmbeddingParam.builder()
                    .apiKey(embeddingConfig.getApiKey())
                    .model(embeddingConfig.getModel())
                    .texts(List.of(text))
                    .build();

            TextEmbedding textEmbedding = new TextEmbedding();
            TextEmbeddingResult result = textEmbedding.call(param);

            if (result != null && result.getOutput() != null
                    && result.getOutput().getEmbeddings() != null
                    && !result.getOutput().getEmbeddings().isEmpty()) {

                List<Double> embeddingList = result.getOutput().getEmbeddings().get(0).getEmbedding();
                float[] embedding = toFloatArray(embeddingList);

                successfulCalls++;
                updateAverageResponseTime(System.currentTimeMillis() - startTime);
                lastCallTime = System.currentTimeMillis();

                if (embeddingConfig.isEnableCache()) {
                    saveToCache(cacheKey, embedding);
                }

                log.debug("文本嵌入成功，耗时: {}ms, 向量维度: {}",
                        System.currentTimeMillis() - startTime, embedding.length);

                return embedding;
            } else {
                failedCalls++;
                log.warn("文本嵌入返回空结果: text长度={}", text.length());
                return new float[embeddingConfig.getDimension()];
            }

        } catch (Exception e) {
            failedCalls++;
            log.error("文本嵌入API调用异常: {}", e.getMessage(), e);

            if (e instanceof ApiException && shouldRetry(e)) {
                return retryEmbed(text);
            }

            throw new RuntimeException("文本嵌入失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<float[]> batchEmbed(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return new ArrayList<>();
        }

        List<float[]> results = new ArrayList<>();
        List<String> uncachedTexts = new ArrayList<>();
        List<Integer> uncachedIndices = new ArrayList<>();

        if (embeddingConfig.isEnableCache()) {
            for (int i = 0; i < texts.size(); i++) {
                String text = texts.get(i);
                String cacheKey = generateCacheKey(text);
                float[] cached = getFromCache(cacheKey);

                if (cached != null) {
                    results.add(cached);
                    cacheHits++;
                } else {
                    results.add(null);
                    uncachedTexts.add(text);
                    uncachedIndices.add(i);
                    cacheMisses++;
                }
            }
        } else {
            uncachedTexts.addAll(texts);
            for (int i = 0; i < texts.size(); i++) {
                uncachedIndices.add(i);
                results.add(null);
            }
        }

        if (!uncachedTexts.isEmpty()) {
            long startTime = System.currentTimeMillis();

            try {
                totalCalls++;

                TextEmbeddingParam param = TextEmbeddingParam.builder()
                        .apiKey(embeddingConfig.getApiKey())
                        .model(embeddingConfig.getModel())
                        .texts(uncachedTexts)
                        .build();

                TextEmbedding textEmbedding = new TextEmbedding();
                TextEmbeddingResult result = textEmbedding.call(param);

                if (result != null && result.getOutput() != null
                        && result.getOutput().getEmbeddings() != null) {

                    List<?> embeddings = result.getOutput().getEmbeddings();

                    for (int i = 0; i < embeddings.size() && i < uncachedIndices.size(); i++) {
                        Object embObj = embeddings.get(i);
                        List<Double> embeddingList = getEmbeddingFromObject(embObj);
                        if (embeddingList != null) {
                            float[] embedding = toFloatArray(embeddingList);

                            int originalIndex = uncachedIndices.get(i);
                            results.set(originalIndex, embedding);

                            if (embeddingConfig.isEnableCache()) {
                                String cacheKey = generateCacheKey(uncachedTexts.get(i));
                                saveToCache(cacheKey, embedding);
                            }
                        }
                    }

                    successfulCalls++;
                    updateAverageResponseTime(System.currentTimeMillis() - startTime);
                    lastCallTime = System.currentTimeMillis();

                    log.debug("批量文本嵌入成功，数量: {}, 耗时: {}ms",
                            uncachedTexts.size(), System.currentTimeMillis() - startTime);
                } else {
                    failedCalls++;
                    log.warn("批量文本嵌入返回空结果");
                }

            } catch (Exception e) {
                failedCalls++;
                log.error("批量文本嵌入失败: {}", e.getMessage(), e);
                throw new RuntimeException("批量文本嵌入失败: " + e.getMessage(), e);
            }
        }

        return results;
    }

    @Override
    public double cosineSimilarity(float[] vector1, float[] vector2) {
        if (vector1 == null || vector2 == null || vector1.length != vector2.length) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += vector1[i] * vector1[i];
            norm2 += vector2[i] * vector2[i];
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @Override
    public double textSimilarity(String text1, String text2) {
        float[] embedding1 = embed(text1);
        float[] embedding2 = embed(text2);
        return cosineSimilarity(embedding1, embedding2);
    }

    @Override
    public boolean isHealthy() {
        try {
            float[] embedding = embed("健康检查");
            return embedding != null && embedding.length == embeddingConfig.getDimension();
        } catch (Exception e) {
            log.warn("Embedding API健康检查失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public int getDimension() {
        return embeddingConfig.getDimension();
    }

    @Override
    public EmbeddingStats getStats() {
        return new EmbeddingStats(totalCalls, successfulCalls, failedCalls,
                averageResponseTime, lastCallTime, cacheHits, cacheMisses);
    }

    private float[] retryEmbed(String text) {
        for (int i = 0; i < embeddingConfig.getMaxRetries(); i++) {
            try {
                log.info("第{}次重试文本嵌入", i + 1);
                Thread.sleep(embeddingConfig.getRetryIntervalMs());
                return embed(text);
            } catch (Exception e) {
                log.warn("第{}次重试失败: {}", i + 1, e.getMessage());
                if (i == embeddingConfig.getMaxRetries() - 1) {
                    throw new RuntimeException("重试后仍然失败: " + e.getMessage(), e);
                }
            }
        }
        return new float[embeddingConfig.getDimension()];
    }

    private boolean shouldRetry(Exception e) {
        String message = e.getMessage().toLowerCase();
        return message.contains("timeout") ||
                message.contains("connection") ||
                message.contains("network") ||
                message.contains("rate limit");
    }

    private String generateCacheKey(String text) {
        return CACHE_PREFIX + Base64.getEncoder().encodeToString(text.getBytes());
    }

    private float[] getFromCache(String key) {
        try {
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached instanceof float[]) {
                return (float[]) cached;
            }
        } catch (Exception e) {
            log.debug("从缓存获取向量失败: {}", e.getMessage());
        }
        return null;
    }

    private void saveToCache(String key, float[] embedding) {
        try {
            redisTemplate.opsForValue().set(key, embedding,
                    embeddingConfig.getCacheExpireHours(), TimeUnit.HOURS);
        } catch (Exception e) {
            log.debug("保存向量到缓存失败: {}", e.getMessage());
        }
    }

    private float[] toFloatArray(List<Double> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).floatValue();
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    private List<Double> getEmbeddingFromObject(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            if (!list.isEmpty()) {
                Object first = list.get(0);
                if (first instanceof Number) {
                    return (List<Double>) obj;
                } else if (first instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) first;
                    Object embedding = map.get("embedding");
                    if (embedding instanceof List) {
                        return (List<Double>) embedding;
                    }
                }
            }
        } else if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            Object embedding = map.get("embedding");
            if (embedding instanceof List) {
                return (List<Double>) embedding;
            }
        }
        return null;
    }

    private synchronized void updateAverageResponseTime(long responseTime) {
        if (successfulCalls == 1) {
            averageResponseTime = responseTime;
        } else {
            averageResponseTime = (averageResponseTime * (successfulCalls - 1) + responseTime) / successfulCalls;
        }
    }
}
