package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.dto.VideoVectorizationRequest;
import com.xailab.vehicle.xaivehicledata.dto.VideoVectorizationResponse;

import java.util.concurrent.CompletableFuture;

/**
 * 视频向量化处理服务
 */
public interface VideoVectorizationService {

    /**
     * 批量向量化所有视频
     *
     * @param request 处理请求
     * @return 处理响应
     */
    VideoVectorizationResponse batchVectorize(VideoVectorizationRequest request);

    /**
     * 异步批量向量化所有视频
     *
     * @param request 处理请求
     * @return 异步处理结果
     */
    CompletableFuture<VideoVectorizationResponse> batchVectorizeAsync(VideoVectorizationRequest request);

    /**
     * 增量向量化单个视频
     *
     * @param videoId     视频ID
     * @param forceUpdate 是否强制更新
     * @return 处理响应
     */
    VideoVectorizationResponse.VideoProcessResult vectorizeSingle(Long videoId, boolean forceUpdate);

    /**
     * 获取处理状态
     *
     * @param taskId 任务ID
     * @return 处理响应
     */
    VideoVectorizationResponse getProcessStatus(String taskId);

    /**
     * 获取未处理的视频数量
     *
     * @return 未处理数量
     */
    int getUnprocessedCount();

    /**
     * 检查视频是否已向量化
     *
     * @param videoId 视频ID
     * @return 是否已向量化
     */
    boolean isVectorized(Long videoId);
}
