package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.dto.VideoVectorSearchResult;
import com.xailab.vehicle.xaivehicledata.entity.VideoVectorStoreEntity;

import java.util.List;

/**
 * 视频向量存储服务
 */
public interface VideoVectorStoreService {

    /**
     * 保存视频向量
     *
     * @param entity 视频向量实体
     * @return 是否成功
     */
    boolean save(VideoVectorStoreEntity entity);

    /**
     * 批量保存视频向量
     *
     * @param entities 视频向量实体列表
     * @return 成功数量
     */
    int batchSave(List<VideoVectorStoreEntity> entities);

    /**
     * 根据视频ID获取向量
     *
     * @param videoId 视频ID
     * @return 向量实体
     */
    VideoVectorStoreEntity getByVideoId(Long videoId);

    /**
     * 更新视频向量
     *
     * @param entity 视频向量实体
     * @return 是否成功
     */
    boolean update(VideoVectorStoreEntity entity);

    /**
     * 删除视频向量
     *
     * @param videoId 视频ID
     * @return 是否成功
     */
    boolean delete(Long videoId);

    /**
     * 向量相似度搜索
     *
     * @param queryVector 查询向量
     * @param topK        返回数量
     * @param threshold   相似度阈值
     * @return 搜索结果列表
     */
    List<VideoVectorSearchResult> searchSimilar(float[] queryVector, int topK, double threshold);

    /**
     * 向量相似度搜索（带过滤条件）
     *
     * @param queryVector      查询向量
     * @param vehicleId        车辆ID（可选）
     * @param functionDomainId 功能域ID（可选）
     * @param videoType        视频类型（可选）
     * @param topK             返回数量
     * @param threshold        相似度阈值
     * @return 搜索结果列表
     */
    List<VideoVectorSearchResult> searchSimilarWithFilter(
            float[] queryVector,
            Long vehicleId,
            Long functionDomainId,
            Integer videoType,
            int topK,
            double threshold);

    /**
     * 混合搜索（向量 + 全文）
     *
     * @param queryText   查询文本
     * @param queryVector 查询向量
     * @param topK        返回数量
     * @param threshold   相似度阈值
     * @return 搜索结果列表
     */
    List<VideoVectorSearchResult> hybridSearch(String queryText, float[] queryVector, int topK, double threshold);

    /**
     * 获取车辆相关的所有视频向量
     *
     * @param vehicleId 车辆ID
     * @return 向量实体列表
     */
    List<VideoVectorStoreEntity> getByVehicleId(Long vehicleId);

    /**
     * 获取功能域相关的所有视频向量
     *
     * @param functionDomainId 功能域ID
     * @return 向量实体列表
     */
    List<VideoVectorStoreEntity> getByFunctionDomainId(Long functionDomainId);

    /**
     * 获取统计信息
     *
     * @return 统计信息
     */
    VectorStoreStats getStats();

    /**
     * 向量存储统计信息
     */
    record VectorStoreStats(
            long totalVectors,
            long totalVehicles,
            long totalDomains,
            long goodCaseCount,
            long badCaseCount,
            double avgScore) {
    }
}
