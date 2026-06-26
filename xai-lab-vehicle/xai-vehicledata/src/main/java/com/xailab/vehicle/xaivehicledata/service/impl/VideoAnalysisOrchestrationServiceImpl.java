package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaivehicledata.dao.VideoAnalysisResultDao;
import com.xailab.vehicle.xaivehicledata.entity.FunctionDomainVideoEntity;
import com.xailab.vehicle.xaivehicledata.entity.VideoAnalysisResultEntity;
import com.xailab.vehicle.xaivehicledata.entity.dto.TranscriptResult;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisRequest;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisResult;
import com.xailab.vehicle.xaivehicledata.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoAnalysisOrchestrationServiceImpl implements VideoAnalysisOrchestrationService {

    private final FunctionDomainVideoService functionDomainVideoService;
    private final VideoRetrievalService videoRetrievalService;
    private final VideoPreprocessService videoPreprocessService;
    private final MultimodalAnalysisService multimodalAnalysisService;
    private final VideoAnalysisResultDao videoAnalysisResultDao;

    private final Map<String, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();

    @Async("videoAnalysisExecutor")
    @Override
    public CompletableFuture<VideoAnalysisResult> analyzeVideoAsync(Long videoId) {
        log.info("开始异步分析视频: videoId={}", videoId);
        String taskId = UUID.randomUUID().toString();

        TaskStatus status = new TaskStatus(taskId, "processing", 0, "开始分析", null);
        taskStatusMap.put(taskId, status);

        String videoPath = null;
        String decryptedPath = null;
        String audioPath = null;
        List<String> frames = new ArrayList<>();

        try {
            status.setProgress(10);
            status.setMessage("获取视频信息");

            FunctionDomainVideoEntity video = functionDomainVideoService.getById(videoId);
            if (video == null) {
                throw new RuntimeException("视频不存在: " + videoId);
            }

            status.setProgress(20);
            status.setMessage("下载视频");

            videoPath = videoRetrievalService.downloadEncryptedVideo(videoId);

            status.setProgress(30);
            status.setMessage("解密视频");

            decryptedPath = videoRetrievalService.decryptVideo(videoPath);

            status.setProgress(40);
            status.setMessage("提取音频");

            audioPath = videoPreprocessService.extractAudio(decryptedPath);

            status.setProgress(50);
            status.setMessage("截取关键帧");

            frames = videoPreprocessService.captureFrames(decryptedPath, Arrays.asList(0.5));

            status.setProgress(60);
            status.setMessage("语音转文字");

            TranscriptResult transcript = multimodalAnalysisService.transcribeAudio(audioPath);

            status.setProgress(80);
            status.setMessage("多模态分析");

            VideoAnalysisRequest request = VideoAnalysisRequest.builder()
                    .videoId(videoId)
                    .transcript(transcript.getText())
                    .framePaths(frames)
                    .vehicleName("车辆" + video.getVehicleId())
                    .domainName("功能域" + video.getFunctionDomainId())
                    .indexName("指标" + video.getFunctionDomainIndexId())
                    .videoType(video.getType() == 1 ? "good" : "bad")
                    .taskType(video.getTaskType())
                    .description(video.getDescription())
                    .build();

            VideoAnalysisResult result = multimodalAnalysisService.analyzeVideo(request);

            status.setProgress(90);
            status.setMessage("保存结果");

            saveAnalysisResult(video, result, transcript);

            status.setStatus("completed");
            status.setProgress(100);
            status.setMessage("分析完成");
            status.setResult(result);

            log.info("视频分析完成: videoId={}", videoId);
            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            log.error("视频分析失败: videoId={}, error={}", videoId, e.getMessage(), e);

            status.setStatus("failed");
            status.setMessage("分析失败: " + e.getMessage());

            saveFailedResult(videoId, e.getMessage());

            return CompletableFuture.failedFuture(e);
        } finally {
            cleanupTempFiles(videoPath, decryptedPath, audioPath, frames);
        }
    }

    @Override
    public List<String> analyzeVideosBatch(List<Long> videoIds) {
        log.info("批量分析视频: count={}", videoIds.size());
        List<String> taskIds = new ArrayList<>();

        for (Long videoId : videoIds) {
            CompletableFuture<VideoAnalysisResult> future = analyzeVideoAsync(videoId);
            taskIds.add(videoId.toString());
        }

        return taskIds;
    }

    @Override
    public VideoAnalysisResult getAnalysisResult(Long videoId) {
        VideoAnalysisResultEntity entity = videoAnalysisResultDao.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<VideoAnalysisResultEntity>()
                        .eq("video_id", videoId)
                        .eq("status", "completed")
                        .orderByDesc("create_time")
                        .last("LIMIT 1"));

        if (entity == null) {
            return null;
        }

        return convertToDto(entity);
    }

    @Override
    public TaskStatus checkAnalysisStatus(String taskId) {
        return taskStatusMap.get(taskId);
    }

    @Override
    public boolean isVideoAnalyzed(Long videoId) {
        Long count = videoAnalysisResultDao.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<VideoAnalysisResultEntity>()
                        .eq("video_id", videoId)
                        .eq("status", "completed"));
        return count != null && count > 0;
    }

    private void saveAnalysisResult(FunctionDomainVideoEntity video, VideoAnalysisResult result,
            TranscriptResult transcript) {
        VideoAnalysisResultEntity entity = new VideoAnalysisResultEntity();
        entity.setVideoId(video.getId());
        entity.setVehicleId(video.getVehicleId());
        entity.setDomainId(video.getFunctionDomainId());
        entity.setIndexId(video.getFunctionDomainIndexId());
        entity.setTranscript(transcript.getText());
        entity.setTranscriptConfidence(transcript.getConfidence());
        entity.setEvaluationScore(result.getOverallScore());
        entity.setEvaluationSummary(result.getSummary());
        entity.setImprovementSuggestions(
                result.getImprovementSuggestions() != null ? String.join(";", result.getImprovementSuggestions()) : "");
        entity.setAnalysisModel("qwen-vl-max");
        entity.setProcessingTimeMs(result.getProcessingTimeMs());
        entity.setStatus("completed");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        try {
            entity.setEvaluationDimensions(com.alibaba.fastjson.JSON.toJSONString(result.getDimensions()));
        } catch (Exception e) {
            log.warn("序列化评估维度失败: {}", e.getMessage());
        }

        videoAnalysisResultDao.insert(entity);
    }

    private void saveFailedResult(Long videoId, String errorMessage) {
        VideoAnalysisResultEntity entity = new VideoAnalysisResultEntity();
        entity.setVideoId(videoId);
        entity.setStatus("failed");
        entity.setErrorMessage(errorMessage);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        videoAnalysisResultDao.insert(entity);
    }

    private VideoAnalysisResult convertToDto(VideoAnalysisResultEntity entity) {
        Map<String, VideoAnalysisResult.DimensionScore> dimensions = new HashMap<>();
        try {
            if (entity.getEvaluationDimensions() != null) {
                Map<String, Object> dimMap = com.alibaba.fastjson.JSON.parseObject(
                        entity.getEvaluationDimensions(), Map.class);
                for (Map.Entry<String, Object> entry : dimMap.entrySet()) {
                    if (entry.getValue() instanceof Map) {
                        Map<String, Object> dimData = (Map<String, Object>) entry.getValue();
                        dimensions.put(entry.getKey(), VideoAnalysisResult.DimensionScore.builder()
                                .score(((Number) dimData.get("score")).doubleValue())
                                .comment((String) dimData.get("comment"))
                                .build());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("解析评估维度失败: {}", e.getMessage());
        }

        return VideoAnalysisResult.builder()
                .videoId(entity.getVideoId())
                .overallScore(entity.getEvaluationScore())
                .dimensions(dimensions)
                .summary(entity.getEvaluationSummary())
                .improvementSuggestions(entity.getImprovementSuggestions() != null
                        ? Arrays.asList(entity.getImprovementSuggestions().split(";"))
                        : new ArrayList<>())
                .transcript(entity.getTranscript())
                .processingTimeMs(entity.getProcessingTimeMs())
                .build();
    }

    private void cleanupTempFiles(String videoPath, String decryptedPath, String audioPath, List<String> frames) {
        if (videoPath != null) {
            videoRetrievalService.cleanupTempFile(videoPath);
        }
        if (decryptedPath != null) {
            videoRetrievalService.cleanupTempFile(decryptedPath);
        }
        if (audioPath != null) {
            videoPreprocessService.cleanupTempFiles(Arrays.asList(audioPath));
        }
        if (frames != null && !frames.isEmpty()) {
            videoPreprocessService.cleanupTempFiles(frames);
        }
    }
}
