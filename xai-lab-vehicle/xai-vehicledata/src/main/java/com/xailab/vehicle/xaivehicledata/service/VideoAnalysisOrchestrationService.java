package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VideoAnalysisOrchestrationService {

    CompletableFuture<VideoAnalysisResult> analyzeVideoAsync(Long videoId);

    List<String> analyzeVideosBatch(List<Long> videoIds);

    VideoAnalysisResult getAnalysisResult(Long videoId);

    TaskStatus checkAnalysisStatus(String taskId);

    boolean isVideoAnalyzed(Long videoId);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class TaskStatus {
        private String taskId;
        private String status;
        private Integer progress;
        private String message;
        private VideoAnalysisResult result;
    }
}
