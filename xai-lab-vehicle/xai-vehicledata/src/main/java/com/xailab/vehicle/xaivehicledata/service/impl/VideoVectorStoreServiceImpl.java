package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xailab.vehicle.xaivehicledata.config.EmbeddingConfig;
import com.xailab.vehicle.xaivehicledata.dto.VideoVectorSearchResult;
import com.xailab.vehicle.xaivehicledata.entity.VideoVectorStoreEntity;
import com.xailab.vehicle.xaivehicledata.mapper.VideoVectorStoreMapper;
import com.xailab.vehicle.xaivehicledata.service.VideoVectorStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频向量存储服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoVectorStoreServiceImpl implements VideoVectorStoreService {

    private final VideoVectorStoreMapper vectorStoreMapper;
    private final JdbcTemplate jdbcTemplate;
    private final EmbeddingConfig embeddingConfig;

    @Override
    @Transactional
    public boolean save(VideoVectorStoreEntity entity) {
        try {
            int result = vectorStoreMapper.insert(entity);
            log.debug("保存视频向量成功: videoId={}", entity.getVideoId());
            return result > 0;
        } catch (Exception e) {
            log.error("保存视频向量失败: videoId={}, error={}", entity.getVideoId(), e.getMessage(), e);
            throw new RuntimeException("保存视频向量失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public int batchSave(List<VideoVectorStoreEntity> entities) {
        int successCount = 0;
        for (VideoVectorStoreEntity entity : entities) {
            try {
                if (save(entity)) {
                    successCount++;
                }
            } catch (Exception e) {
                log.warn("批量保存时单个失败: videoId={}, error={}", entity.getVideoId(), e.getMessage());
            }
        }
        log.info("批量保存视频向量完成: 总数={}, 成功={}", entities.size(), successCount);
        return successCount;
    }

    @Override
    public VideoVectorStoreEntity getByVideoId(Long videoId) {
        return vectorStoreMapper.selectByVideoId(videoId);
    }

    @Override
    @Transactional
    public boolean update(VideoVectorStoreEntity entity) {
        try {
            VideoVectorStoreEntity existing = getByVideoId(entity.getVideoId());
            if (existing == null) {
                log.warn("视频向量不存在，无法更新: videoId={}", entity.getVideoId());
                return false;
            }
            entity.setId(existing.getId());
            int result = vectorStoreMapper.updateById(entity);
            log.debug("更新视频向量成功: videoId={}", entity.getVideoId());
            return result > 0;
        } catch (Exception e) {
            log.error("更新视频向量失败: videoId={}, error={}", entity.getVideoId(), e.getMessage(), e);
            throw new RuntimeException("更新视频向量失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean delete(Long videoId) {
        try {
            VideoVectorStoreEntity existing = getByVideoId(videoId);
            if (existing == null) {
                log.warn("视频向量不存在，无法删除: videoId={}", videoId);
                return false;
            }
            int result = vectorStoreMapper.deleteById(existing.getId());
            log.debug("删除视频向量成功: videoId={}", videoId);
            return result > 0;
        } catch (Exception e) {
            log.error("删除视频向量失败: videoId={}, error={}", videoId, e.getMessage(), e);
            throw new RuntimeException("删除视频向量失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<VideoVectorSearchResult> searchSimilar(float[] queryVector, int topK, double threshold) {
        return searchSimilarWithFilter(queryVector, null, null, null, topK, threshold);
    }

    @Override
    public List<VideoVectorSearchResult> searchSimilarWithFilter(
            float[] queryVector,
            Long vehicleId,
            Long functionDomainId,
            Integer videoType,
            int topK,
            double threshold) {

        try {
            String vectorStr = VideoVectorStoreEntity.vectorToString(queryVector);

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT video_id, vehicle_id, function_domain_id, function_domain_index_id, ");
            sql.append("summary_text, transcript_text, analysis_text, keyframe_text, ");
            sql.append("video_type, task_type, overall_score, ");
            sql.append("1 - (summary_embedding <=> ?::vector) AS similarity ");
            sql.append("FROM video_vector_store ");
            sql.append("WHERE 1 - (summary_embedding <=> ?::vector) > ? ");

            List<Object> params = new ArrayList<>();
            params.add(vectorStr);
            params.add(vectorStr);
            params.add(threshold);

            if (vehicleId != null) {
                sql.append("AND vehicle_id = ? ");
                params.add(vehicleId);
            }
            if (functionDomainId != null) {
                sql.append("AND function_domain_id = ? ");
                params.add(functionDomainId);
            }
            if (videoType != null) {
                sql.append("AND video_type = ? ");
                params.add(videoType);
            }

            sql.append("ORDER BY summary_embedding <=> ?::vector ");
            sql.append("LIMIT ?");
            params.add(vectorStr);
            params.add(topK);

            log.debug("执行向量搜索SQL: {}", sql);

            return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
                return VideoVectorSearchResult.builder()
                        .videoId(rs.getLong("video_id"))
                        .vehicleId(rs.getLong("vehicle_id"))
                        .functionDomainId(rs.getLong("function_domain_id"))
                        .functionDomainIndexId(rs.getLong("function_domain_index_id"))
                        .summaryText(rs.getString("summary_text"))
                        .transcriptText(rs.getString("transcript_text"))
                        .analysisText(rs.getString("analysis_text"))
                        .keyframeText(rs.getString("keyframe_text"))
                        .videoType(rs.getInt("video_type"))
                        .taskType(rs.getString("task_type"))
                        .overallScore(rs.getBigDecimal("overall_score"))
                        .similarity(rs.getDouble("similarity"))
                        .build();
            });

        } catch (Exception e) {
            log.error("向量搜索失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<VideoVectorSearchResult> hybridSearch(String queryText, float[] queryVector, int topK,
            double threshold) {
        try {
            String vectorStr = VideoVectorStoreEntity.vectorToString(queryVector);

            String sql = """
                    SELECT
                        video_id, vehicle_id, function_domain_id, function_domain_index_id,
                        summary_text, transcript_text, analysis_text, keyframe_text,
                        video_type, task_type, overall_score,
                        (1 - (summary_embedding <=> ?::vector)) AS vector_similarity,
                        ts_rank_cd(to_tsvector('chinese', summary_text), plainto_tsquery('chinese', ?)) AS text_rank,
                        (0.7 * (1 - (summary_embedding <=> ?::vector)) +
                         0.3 * ts_rank_cd(to_tsvector('chinese', summary_text), plainto_tsquery('chinese', ?))) AS combined_score
                    FROM video_vector_store
                    WHERE
                        (1 - (summary_embedding <=> ?::vector)) > ?
                        OR to_tsvector('chinese', summary_text) @@ plainto_tsquery('chinese', ?)
                    ORDER BY combined_score DESC
                    LIMIT ?
                    """;

            log.debug("执行混合搜索: queryText={}", queryText);

            return jdbcTemplate.query(sql,
                    ps -> {
                        ps.setString(1, vectorStr);
                        ps.setString(2, queryText);
                        ps.setString(3, vectorStr);
                        ps.setString(4, queryText);
                        ps.setString(5, vectorStr);
                        ps.setDouble(6, threshold);
                        ps.setString(7, queryText);
                        ps.setInt(8, topK);
                    },
                    (rs, rowNum) -> {
                        return VideoVectorSearchResult.builder()
                                .videoId(rs.getLong("video_id"))
                                .vehicleId(rs.getLong("vehicle_id"))
                                .functionDomainId(rs.getLong("function_domain_id"))
                                .functionDomainIndexId(rs.getLong("function_domain_index_id"))
                                .summaryText(rs.getString("summary_text"))
                                .transcriptText(rs.getString("transcript_text"))
                                .analysisText(rs.getString("analysis_text"))
                                .keyframeText(rs.getString("keyframe_text"))
                                .videoType(rs.getInt("video_type"))
                                .taskType(rs.getString("task_type"))
                                .overallScore(rs.getBigDecimal("overall_score"))
                                .similarity(rs.getDouble("vector_similarity"))
                                .textRank(rs.getDouble("text_rank"))
                                .combinedScore(rs.getDouble("combined_score"))
                                .build();
                    });

        } catch (Exception e) {
            log.error("混合搜索失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<VideoVectorStoreEntity> getByVehicleId(Long vehicleId) {
        LambdaQueryWrapper<VideoVectorStoreEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoVectorStoreEntity::getVehicleId, vehicleId)
                .orderByDesc(VideoVectorStoreEntity::getOverallScore);
        return vectorStoreMapper.selectList(wrapper);
    }

    @Override
    public List<VideoVectorStoreEntity> getByFunctionDomainId(Long functionDomainId) {
        LambdaQueryWrapper<VideoVectorStoreEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoVectorStoreEntity::getFunctionDomainId, functionDomainId)
                .orderByDesc(VideoVectorStoreEntity::getOverallScore);
        return vectorStoreMapper.selectList(wrapper);
    }

    @Override
    public VectorStoreStats getStats() {
        try {
            String sql = "SELECT * FROM video_vector_stats";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                return new VectorStoreStats(
                        rs.getLong("total_videos"),
                        rs.getLong("total_vehicles"),
                        rs.getLong("total_domains"),
                        rs.getLong("good_case_count"),
                        rs.getLong("bad_case_count"),
                        rs.getDouble("avg_score"));
            });
        } catch (Exception e) {
            log.warn("获取统计信息失败: {}", e.getMessage());
            return new VectorStoreStats(0, 0, 0, 0, 0, 0.0);
        }
    }
}
