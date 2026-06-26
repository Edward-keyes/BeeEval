package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.dto.VideoVectorSearchResult;

import java.util.List;

/**
 * 视频向量检索服务
 */
public interface VideoVectorSearchService {

    /**
     * 根据问题检索相关视频
     *
     * @param question 用户问题
     * @param topK     返回数量
     * @return 相关视频列表
     */
    List<VideoVectorSearchResult> searchByQuestion(String question, int topK);

    /**
     * 根据问题检索相关视频（带过滤条件）
     *
     * @param question         用户问题
     * @param vehicleId        车辆ID（可选）
     * @param functionDomainId 功能域ID（可选）
     * @param videoType        视频类型（可选）
     * @param topK             返回数量
     * @return 相关视频列表
     */
    List<VideoVectorSearchResult> searchByQuestionWithFilter(
            String question,
            Long vehicleId,
            Long functionDomainId,
            Integer videoType,
            int topK);

    /**
     * 混合检索（向量 + 全文）
     *
     * @param question 用户问题
     * @param topK     返回数量
     * @return 相关视频列表
     */
    List<VideoVectorSearchResult> hybridSearch(String question, int topK);

    /**
     * 根据视频ID查找相似视频
     *
     * @param videoId 视频ID
     * @param topK    返回数量
     * @return 相似视频列表
     */
    List<VideoVectorSearchResult> findSimilarVideos(Long videoId, int topK);

    /**
     * 根据文本查找相似视频
     *
     * @param text 文本内容
     * @param topK 返回数量
     * @return 相似视频列表
     */
    List<VideoVectorSearchResult> findSimilarByText(String text, int topK);

    /**
     * 获取视频检索上下文（用于LLM Prompt）
     *
     * @param results 检索结果
     * @return 格式化的上下文文本
     */
    String buildSearchContext(List<VideoVectorSearchResult> results);

    /**
     * 判断问题是否需要视频检索
     *
     * @param question 用户问题
     * @return 是否需要视频检索
     */
    boolean needsVideoSearch(String question);
}
