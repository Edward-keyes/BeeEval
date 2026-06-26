package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaivehicledata.config.EmbeddingConfig;
import com.xailab.vehicle.xaivehicledata.dto.VideoVectorSearchResult;
import com.xailab.vehicle.xaivehicledata.entity.VideoVectorStoreEntity;
import com.xailab.vehicle.xaivehicledata.service.EmbeddingService;
import com.xailab.vehicle.xaivehicledata.service.VideoVectorSearchService;
import com.xailab.vehicle.xaivehicledata.service.VideoVectorStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 视频向量检索服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoVectorSearchServiceImpl implements VideoVectorSearchService {

    private final EmbeddingService embeddingService;
    private final VideoVectorStoreService vectorStoreService;
    private final EmbeddingConfig embeddingConfig;

    private static final Set<String> VIDEO_KEYWORDS = Set.of(
            "视频", "表现", "演示", "案例", "优秀", "问题", "缺陷",
            "驾驶", "测试", "场景", "操作", "行为", "决策",
            "优点", "不足", "改进", "建议", "评价", "评分");

    @Override
    public List<VideoVectorSearchResult> searchByQuestion(String question, int topK) {
        return searchByQuestionWithFilter(question, null, null, null, topK);
    }

    @Override
    public List<VideoVectorSearchResult> searchByQuestionWithFilter(
            String question,
            Long vehicleId,
            Long functionDomainId,
            Integer videoType,
            int topK) {

        try {
            log.debug("开始向量检索: question={}, vehicleId={}, domainId={}, videoType={}",
                    question, vehicleId, functionDomainId, videoType);

            float[] queryVector = embeddingService.embed(question);

            List<VideoVectorSearchResult> results = vectorStoreService.searchSimilarWithFilter(
                    queryVector,
                    vehicleId,
                    functionDomainId,
                    videoType,
                    topK,
                    embeddingConfig.getSimilarityThreshold());

            log.info("向量检索完成: question={}, 结果数量={}", question, results.size());

            return results;

        } catch (Exception e) {
            log.error("向量检索失败: question={}, error={}", question, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<VideoVectorSearchResult> hybridSearch(String question, int topK) {
        try {
            log.debug("开始混合检索: question={}", question);

            float[] queryVector = embeddingService.embed(question);

            List<VideoVectorSearchResult> results = vectorStoreService.hybridSearch(
                    question,
                    queryVector,
                    topK,
                    embeddingConfig.getSimilarityThreshold());

            log.info("混合检索完成: question={}, 结果数量={}", question, results.size());

            return results;

        } catch (Exception e) {
            log.error("混合检索失败: question={}, error={}", question, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<VideoVectorSearchResult> findSimilarVideos(Long videoId, int topK) {
        try {
            log.debug("查找相似视频: videoId={}", videoId);

            VideoVectorStoreEntity videoVector = vectorStoreService.getByVideoId(videoId);
            if (videoVector == null) {
                log.warn("视频向量不存在: videoId={}", videoId);
                return new ArrayList<>();
            }

            float[] queryVector = VideoVectorStoreEntity.stringToVector(videoVector.getSummaryEmbedding());
            if (queryVector == null) {
                log.warn("视频向量为空: videoId={}", videoId);
                return new ArrayList<>();
            }

            List<VideoVectorSearchResult> results = vectorStoreService.searchSimilar(
                    queryVector,
                    topK + 1,
                    embeddingConfig.getSimilarityThreshold());

            results.removeIf(r -> r.getVideoId().equals(videoId));

            if (results.size() > topK) {
                results = results.subList(0, topK);
            }

            log.info("查找相似视频完成: videoId={}, 结果数量={}", videoId, results.size());

            return results;

        } catch (Exception e) {
            log.error("查找相似视频失败: videoId={}, error={}", videoId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<VideoVectorSearchResult> findSimilarByText(String text, int topK) {
        try {
            log.debug("根据文本查找相似视频: text长度={}", text.length());

            float[] queryVector = embeddingService.embed(text);

            List<VideoVectorSearchResult> results = vectorStoreService.searchSimilar(
                    queryVector,
                    topK,
                    embeddingConfig.getSimilarityThreshold());

            log.info("根据文本查找相似视频完成: 结果数量={}", results.size());

            return results;

        } catch (Exception e) {
            log.error("根据文本查找相似视频失败: error={}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public String buildSearchContext(List<VideoVectorSearchResult> results) {
        if (results == null || results.isEmpty()) {
            return "未找到相关的视频分析结果。";
        }

        StringBuilder context = new StringBuilder();
        context.append("以下是相关的视频分析结果：\n\n");

        for (int i = 0; i < results.size(); i++) {
            VideoVectorSearchResult result = results.get(i);

            context.append(String.format("【视频%d】ID: %d\n", i + 1, result.getVideoId()));
            context.append(String.format("类型: %s\n", result.getVideoTypeName()));

            if (result.getTaskType() != null) {
                context.append(String.format("任务类型: %s\n", result.getTaskType()));
            }

            if (result.getOverallScore() != null) {
                context.append(String.format("综合评分: %.2f\n", result.getOverallScore()));
            }

            if (result.getSimilarity() != null) {
                context.append(String.format("相关度: %.2f%%\n", result.getSimilarity() * 100));
            }

            if (result.getSummaryText() != null && !result.getSummaryText().isEmpty()) {
                context.append("摘要:\n").append(result.getSummaryText()).append("\n");
            }

            if (result.getAnalysisText() != null && !result.getAnalysisText().isEmpty()) {
                context.append("分析:\n").append(result.getAnalysisText()).append("\n");
            }

            context.append("\n");
        }

        return context.toString();
    }

    @Override
    public boolean needsVideoSearch(String question) {
        if (question == null || question.isEmpty()) {
            return false;
        }

        String lowerQuestion = question.toLowerCase();

        for (String keyword : VIDEO_KEYWORDS) {
            if (lowerQuestion.contains(keyword)) {
                log.debug("问题包含视频相关关键词: {}, 需要视频检索", keyword);
                return true;
            }
        }

        return false;
    }
}
