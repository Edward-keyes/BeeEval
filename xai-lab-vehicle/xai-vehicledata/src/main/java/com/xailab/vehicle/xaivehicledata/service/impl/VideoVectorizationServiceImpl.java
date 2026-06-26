package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xailab.vehicle.xaivehicledata.dao.FunctionDomainVideoDao;
import com.xailab.vehicle.xaivehicledata.dao.VideoAnalysisResultDao;
import com.xailab.vehicle.xaivehicledata.dto.VideoVectorizationRequest;
import com.xailab.vehicle.xaivehicledata.dto.VideoVectorizationResponse;
import com.xailab.vehicle.xaivehicledata.entity.FunctionDomainVideoEntity;
import com.xailab.vehicle.xaivehicledata.entity.VideoAnalysisResultEntity;
import com.xailab.vehicle.xaivehicledata.entity.VideoVectorStoreEntity;
import com.xailab.vehicle.xaivehicledata.service.EmbeddingService;
import com.xailab.vehicle.xaivehicledata.service.VideoVectorStoreService;
import com.xailab.vehicle.xaivehicledata.service.VideoVectorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 视频向量化处理服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoVectorizationServiceImpl implements VideoVectorizationService {

    private final FunctionDomainVideoDao videoMapper;
    private final VideoAnalysisResultDao analysisResultMapper;
    private final VideoVectorStoreService vectorStoreService;
    private final EmbeddingService embeddingService;

    private final Map<String, VideoVectorizationResponse> taskStatusMap = new ConcurrentHashMap<>();

    @Override
    public VideoVectorizationResponse batchVectorize(VideoVectorizationRequest request) {
        String taskId = UUID.randomUUID().toString();

        VideoVectorizationResponse response = VideoVectorizationResponse.builder()
                .taskId(taskId)
                .status("processing")
                .startTime(LocalDateTime.now())
                .processedCount(0)
                .successCount(0)
                .failedCount(0)
                .results(new ArrayList<>())
                .build();

        taskStatusMap.put(taskId, response);

        try {
            List<Long> videoIds = getVideoIdsToProcess(request);
            response.setTotalVideos(videoIds.size());

            log.info("开始批量向量化处理: taskId={}, 总数={}", taskId, videoIds.size());

            for (Long videoId : videoIds) {
                try {
                    VideoVectorizationResponse.VideoProcessResult result = vectorizeSingle(videoId,
                            Boolean.TRUE.equals(request.getForceUpdate()));

                    response.getResults().add(result);
                    response.setProcessedCount(response.getProcessedCount() + 1);

                    if (Boolean.TRUE.equals(result.getSuccess())) {
                        response.setSuccessCount(response.getSuccessCount() + 1);
                    } else {
                        response.setFailedCount(response.getFailedCount() + 1);
                    }

                    taskStatusMap.put(taskId, response);

                } catch (Exception e) {
                    log.error("处理视频失败: videoId={}, error={}", videoId, e.getMessage(), e);
                    response.getResults().add(VideoVectorizationResponse.VideoProcessResult.builder()
                            .videoId(videoId)
                            .success(false)
                            .message("处理失败: " + e.getMessage())
                            .build());
                    response.setFailedCount(response.getFailedCount() + 1);
                    response.setProcessedCount(response.getProcessedCount() + 1);
                }
            }

            response.setStatus("completed");
            response.setEndTime(LocalDateTime.now());
            taskStatusMap.put(taskId, response);

            log.info("批量向量化处理完成: taskId={}, 成功={}, 失败={}",
                    taskId, response.getSuccessCount(), response.getFailedCount());

            return response;

        } catch (Exception e) {
            log.error("批量向量化处理异常: taskId={}, error={}", taskId, e.getMessage(), e);
            response.setStatus("failed");
            response.setEndTime(LocalDateTime.now());
            taskStatusMap.put(taskId, response);
            return response;
        }
    }

    @Async("videoAnalysisExecutor")
    @Override
    public CompletableFuture<VideoVectorizationResponse> batchVectorizeAsync(VideoVectorizationRequest request) {
        log.info("开始异步批量向量化处理");
        VideoVectorizationResponse response = batchVectorize(request);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public VideoVectorizationResponse.VideoProcessResult vectorizeSingle(Long videoId, boolean forceUpdate) {
        long startTime = System.currentTimeMillis();

        try {
            log.debug("开始向量化视频: videoId={}", videoId);

            if (!forceUpdate && isVectorized(videoId)) {
                log.debug("视频已向量化，跳过: videoId={}", videoId);
                return VideoVectorizationResponse.VideoProcessResult.builder()
                        .videoId(videoId)
                        .success(true)
                        .message("视频已向量化，跳过处理")
                        .processingTimeMs(System.currentTimeMillis() - startTime)
                        .build();
            }

            FunctionDomainVideoEntity video = videoMapper.selectById(videoId);
            if (video == null) {
                return VideoVectorizationResponse.VideoProcessResult.builder()
                        .videoId(videoId)
                        .success(false)
                        .message("视频不存在")
                        .processingTimeMs(System.currentTimeMillis() - startTime)
                        .build();
            }

            VideoAnalysisResultEntity analysisResult = analysisResultMapper.selectByVideoId(videoId);
            if (analysisResult == null) {
                log.warn("视频分析结果不存在，无法向量化: videoId={}", videoId);
                return VideoVectorizationResponse.VideoProcessResult.builder()
                        .videoId(videoId)
                        .success(false)
                        .message("视频分析结果不存在，请先进行视频分析")
                        .processingTimeMs(System.currentTimeMillis() - startTime)
                        .build();
            }

            String summaryText = buildSummaryText(video, analysisResult);
            String transcriptText = analysisResult.getTranscript();
            String analysisText = buildAnalysisText(analysisResult);
            String keyframeText = analysisResult.getVisualDescription();

            log.debug("生成文本: summary长度={}, transcript长度={}, analysis长度={}, keyframe长度={}",
                    summaryText.length(),
                    transcriptText != null ? transcriptText.length() : 0,
                    analysisText.length(),
                    keyframeText != null ? keyframeText.length() : 0);

            float[] summaryEmbedding = embeddingService.embed(summaryText);
            float[] transcriptEmbedding = transcriptText != null && !transcriptText.isEmpty()
                    ? embeddingService.embed(transcriptText)
                    : null;
            float[] analysisEmbedding = embeddingService.embed(analysisText);
            float[] keyframeEmbedding = keyframeText != null && !keyframeText.isEmpty()
                    ? embeddingService.embed(keyframeText)
                    : null;

            VideoVectorStoreEntity vectorEntity = VideoVectorStoreEntity.builder()
                    .videoId(videoId)
                    .vehicleId(video.getVehicleId())
                    .functionDomainId(video.getFunctionDomainId())
                    .functionDomainIndexId(video.getFunctionDomainIndexId())
                    .summaryText(summaryText)
                    .transcriptText(transcriptText)
                    .analysisText(analysisText)
                    .keyframeText(keyframeText)
                    .summaryEmbedding(VideoVectorStoreEntity.vectorToString(summaryEmbedding))
                    .transcriptEmbedding(transcriptEmbedding != null
                            ? VideoVectorStoreEntity.vectorToString(transcriptEmbedding)
                            : null)
                    .analysisEmbedding(VideoVectorStoreEntity.vectorToString(analysisEmbedding))
                    .keyframeEmbedding(keyframeEmbedding != null
                            ? VideoVectorStoreEntity.vectorToString(keyframeEmbedding)
                            : null)
                    .videoType(video.getType())
                    .taskType(video.getTaskType())
                    .overallScore(analysisResult.getEvaluationScore() != null
                            ? BigDecimal.valueOf(analysisResult.getEvaluationScore())
                            : null)
                    .build();

            if (forceUpdate && isVectorized(videoId)) {
                vectorStoreService.update(vectorEntity);
            } else {
                vectorStoreService.save(vectorEntity);
            }

            long processingTime = System.currentTimeMillis() - startTime;
            log.info("视频向量化完成: videoId={}, 耗时={}ms", videoId, processingTime);

            return VideoVectorizationResponse.VideoProcessResult.builder()
                    .videoId(videoId)
                    .success(true)
                    .message("向量化成功")
                    .processingTimeMs(processingTime)
                    .build();

        } catch (Exception e) {
            log.error("视频向量化失败: videoId={}, error={}", videoId, e.getMessage(), e);
            return VideoVectorizationResponse.VideoProcessResult.builder()
                    .videoId(videoId)
                    .success(false)
                    .message("向量化失败: " + e.getMessage())
                    .processingTimeMs(System.currentTimeMillis() - startTime)
                    .build();
        }
    }

    @Override
    public VideoVectorizationResponse getProcessStatus(String taskId) {
        return taskStatusMap.get(taskId);
    }

    @Override
    public int getUnprocessedCount() {
        try {
            LambdaQueryWrapper<FunctionDomainVideoEntity> videoWrapper = new LambdaQueryWrapper<>();
            long totalVideos = videoMapper.selectCount(videoWrapper);

            long vectorizedCount = vectorStoreService.getStats().totalVectors();

            return (int) (totalVideos - vectorizedCount);
        } catch (Exception e) {
            log.error("获取未处理视频数量失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public boolean isVectorized(Long videoId) {
        return vectorStoreService.getByVideoId(videoId) != null;
    }

    private List<Long> getVideoIdsToProcess(VideoVectorizationRequest request) {
        if (request.getVideoIds() != null && !request.getVideoIds().isEmpty()) {
            return request.getVideoIds();
        }

        LambdaQueryWrapper<FunctionDomainVideoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(FunctionDomainVideoEntity::getId);

        if (!Boolean.TRUE.equals(request.getForceUpdate())) {
            wrapper.notIn(FunctionDomainVideoEntity::getId,
                    "SELECT video_id FROM video_vector_store");
        }

        List<FunctionDomainVideoEntity> videos = videoMapper.selectList(wrapper);
        return videos.stream().map(FunctionDomainVideoEntity::getId).toList();
    }

    private String buildSummaryText(FunctionDomainVideoEntity video, VideoAnalysisResultEntity analysisResult) {
        StringBuilder sb = new StringBuilder();

        sb.append("视频类型: ").append(video.getType() == 1 ? "优秀案例" : "问题案例").append("\n");
        sb.append("任务类型: ").append(video.getTaskType()).append("\n");

        if (video.getDescription() != null && !video.getDescription().isEmpty()) {
            sb.append("描述: ").append(video.getDescription()).append("\n");
        }

        if (analysisResult.getEvaluationSummary() != null && !analysisResult.getEvaluationSummary().isEmpty()) {
            sb.append("总结: ").append(analysisResult.getEvaluationSummary()).append("\n");
        }

        if (analysisResult.getVisualDescription() != null && !analysisResult.getVisualDescription().isEmpty()) {
            sb.append("视觉描述: ").append(analysisResult.getVisualDescription()).append("\n");
        }

        return sb.toString();
    }

    private String buildAnalysisText(VideoAnalysisResultEntity analysisResult) {
        StringBuilder sb = new StringBuilder();

        if (analysisResult.getEvaluationScore() != null) {
            sb.append("综合评分: ").append(analysisResult.getEvaluationScore()).append("\n");
        }

        if (analysisResult.getEvaluationDimensions() != null && !analysisResult.getEvaluationDimensions().isEmpty()) {
            sb.append("评估维度: ").append(analysisResult.getEvaluationDimensions()).append("\n");
        }

        if (analysisResult.getImprovementSuggestions() != null
                && !analysisResult.getImprovementSuggestions().isEmpty()) {
            sb.append("改进建议: ").append(analysisResult.getImprovementSuggestions()).append("\n");
        }

        return sb.toString();
    }
}
