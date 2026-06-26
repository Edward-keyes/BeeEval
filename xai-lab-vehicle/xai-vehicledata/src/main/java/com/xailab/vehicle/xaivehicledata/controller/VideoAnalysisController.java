package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaivehicledata.common.ApiResponse;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisResult;
import com.xailab.vehicle.xaivehicledata.service.VideoAnalysisOrchestrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Tag(name = "视频分析接口", description = "视频分析相关API")
@RestController
@RequestMapping("/api/v1/video-analysis")
@RequiredArgsConstructor
public class VideoAnalysisController {

    private final VideoAnalysisOrchestrationService videoAnalysisService;

    @Operation(summary = "分析单个视频", description = "异步分析指定的视频")
    @PostMapping("/analyze/{videoId}")
    public ApiResponse<String> analyzeVideo(
            @Parameter(description = "视频ID") @PathVariable Long videoId) {

        log.info("开始分析视频: videoId={}", videoId);

        CompletableFuture<VideoAnalysisResult> future = videoAnalysisService.analyzeVideoAsync(videoId);

        return ApiResponse.success("视频分析任务已启动，videoId: " + videoId);
    }

    @Operation(summary = "批量分析视频", description = "批量异步分析多个视频")
    @PostMapping("/analyze/batch")
    public ApiResponse<List<String>> analyzeVideosBatch(
            @Parameter(description = "视频ID列表") @RequestBody List<Long> videoIds) {

        log.info("批量分析视频: count={}", videoIds.size());

        List<String> taskIds = videoAnalysisService.analyzeVideosBatch(videoIds);

        return ApiResponse.success(taskIds);
    }

    @Operation(summary = "获取分析结果", description = "获取指定视频的分析结果")
    @GetMapping("/result/{videoId}")
    public ApiResponse<VideoAnalysisResult> getAnalysisResult(
            @Parameter(description = "视频ID") @PathVariable Long videoId) {

        log.info("获取视频分析结果: videoId={}", videoId);

        VideoAnalysisResult result = videoAnalysisService.getAnalysisResult(videoId);

        if (result == null) {
            return ApiResponse.error("视频尚未分析或分析结果不存在");
        }

        return ApiResponse.success(result);
    }

    @Operation(summary = "检查分析状态", description = "检查视频分析任务的状态")
    @GetMapping("/status/{taskId}")
    public ApiResponse<VideoAnalysisOrchestrationService.TaskStatus> checkAnalysisStatus(
            @Parameter(description = "任务ID") @PathVariable String taskId) {

        log.info("检查分析状态: taskId={}", taskId);

        VideoAnalysisOrchestrationService.TaskStatus status = videoAnalysisService.checkAnalysisStatus(taskId);

        if (status == null) {
            return ApiResponse.error("任务不存在");
        }

        return ApiResponse.success(status);
    }

    @Operation(summary = "检查视频是否已分析", description = "检查指定视频是否已完成分析")
    @GetMapping("/check/{videoId}")
    public ApiResponse<Boolean> isVideoAnalyzed(
            @Parameter(description = "视频ID") @PathVariable Long videoId) {

        log.info("检查视频是否已分析: videoId={}", videoId);

        boolean analyzed = videoAnalysisService.isVideoAnalyzed(videoId);

        return ApiResponse.success(analyzed);
    }
}
