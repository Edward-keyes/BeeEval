package com.xailab.vehicle.xaivehicledata.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.xailab.vehicle.xaivehicledata.dto.VideoVectorizationRequest;
import com.xailab.vehicle.xaivehicledata.dto.VideoVectorizationResponse;
import com.xailab.vehicle.xaivehicledata.service.VideoVectorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 视频向量化处理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/video-vectorization")
@RequiredArgsConstructor
@Tag(name = "视频向量化处理", description = "视频向量化处理相关接口")
public class VideoVectorizationController {

    private final VideoVectorizationService vectorizationService;

    @SaCheckLogin
    @PostMapping("/batch")
    @Operation(summary = "批量向量化所有视频", description = "批量处理所有视频，生成向量并存储到向量数据库")
    public ResponseEntity<VideoVectorizationResponse> batchVectorize(
            @RequestBody(required = false) VideoVectorizationRequest request) {

        if (request == null) {
            request = VideoVectorizationRequest.all();
        }

        log.info("开始批量向量化处理: videoIds={}, forceUpdate={}",
                request.getVideoIds() != null ? request.getVideoIds().size() : "全部",
                request.getForceUpdate());

        VideoVectorizationResponse response = vectorizationService.batchVectorize(request);
        return ResponseEntity.ok(response);
    }

    @SaCheckLogin
    @PostMapping("/batch-async")
    @Operation(summary = "异步批量向量化所有视频", description = "异步批量处理所有视频，立即返回任务ID")
    public ResponseEntity<VideoVectorizationResponse> batchVectorizeAsync(
            @RequestBody(required = false) VideoVectorizationRequest request) {

        if (request == null) {
            request = VideoVectorizationRequest.all();
        }

        log.info("开始异步批量向量化处理");

        CompletableFuture<VideoVectorizationResponse> future = vectorizationService.batchVectorizeAsync(request);

        String taskId = future.join().getTaskId();

        VideoVectorizationResponse response = VideoVectorizationResponse.builder()
                .taskId(taskId)
                .status("processing")
                .message("任务已提交，请使用taskId查询进度")
                .build();

        return ResponseEntity.accepted().body(response);
    }

    @SaCheckLogin
    @PostMapping("/single/{videoId}")
    @Operation(summary = "向量化单个视频", description = "对单个视频进行向量化处理")
    public ResponseEntity<VideoVectorizationResponse.VideoProcessResult> vectorizeSingle(
            @Parameter(description = "视频ID") @PathVariable Long videoId,
            @Parameter(description = "是否强制更新") @RequestParam(defaultValue = "false") boolean forceUpdate) {

        log.info("开始向量化单个视频: videoId={}, forceUpdate={}", videoId, forceUpdate);

        VideoVectorizationResponse.VideoProcessResult result = vectorizationService.vectorizeSingle(videoId,
                forceUpdate);

        return ResponseEntity.ok(result);
    }

    @SaCheckLogin
    @GetMapping("/status/{taskId}")
    @Operation(summary = "查询处理状态", description = "根据任务ID查询向量化处理状态")
    public ResponseEntity<VideoVectorizationResponse> getProcessStatus(
            @Parameter(description = "任务ID") @PathVariable String taskId) {

        log.debug("查询处理状态: taskId={}", taskId);

        VideoVectorizationResponse response = vectorizationService.getProcessStatus(taskId);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @SaCheckLogin
    @GetMapping("/unprocessed-count")
    @Operation(summary = "获取未处理视频数量", description = "获取尚未向量化的视频数量")
    public ResponseEntity<Integer> getUnprocessedCount() {
        int count = vectorizationService.getUnprocessedCount();
        return ResponseEntity.ok(count);
    }

    @SaCheckLogin
    @GetMapping("/check/{videoId}")
    @Operation(summary = "检查视频是否已向量化", description = "检查指定视频是否已经完成向量化")
    public ResponseEntity<Boolean> isVectorized(
            @Parameter(description = "视频ID") @PathVariable Long videoId) {

        boolean vectorized = vectorizationService.isVectorized(videoId);
        return ResponseEntity.ok(vectorized);
    }

    @SaCheckLogin
    @PostMapping("/incremental")
    @Operation(summary = "增量向量化", description = "对指定的视频列表进行增量向量化处理")
    public ResponseEntity<VideoVectorizationResponse> incrementalVectorize(
            @RequestBody List<Long> videoIds,
            @Parameter(description = "是否强制更新") @RequestParam(defaultValue = "false") boolean forceUpdate) {

        log.info("开始增量向量化处理: videoIds.size={}, forceUpdate={}", videoIds.size(), forceUpdate);

        VideoVectorizationRequest request = VideoVectorizationRequest.builder()
                .videoIds(videoIds)
                .forceUpdate(forceUpdate)
                .includeTranscript(true)
                .includeKeyframes(true)
                .build();

        VideoVectorizationResponse response = vectorizationService.batchVectorize(request);
        return ResponseEntity.ok(response);
    }
}
