package com.xailab.vehicle.xaivehicledata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频向量化处理响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoVectorizationResponse {

    private String taskId;

    private Integer totalVideos;

    private Integer processedCount;

    private Integer successCount;

    private Integer failedCount;

    private String status;

    private String message;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<VideoProcessResult> results;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoProcessResult {
        private Long videoId;
        private Boolean success;
        private String message;
        private Long processingTimeMs;
    }

    public boolean isCompleted() {
        return "completed".equals(status) || "failed".equals(status);
    }

    public double getProgress() {
        if (totalVideos == null || totalVideos == 0) {
            return 0.0;
        }
        return (double) processedCount / totalVideos * 100;
    }
}
