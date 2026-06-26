package com.xailab.vehicle.xaivehicledata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.VideoVectorStoreEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 视频向量存储Mapper
 */
@Mapper
public interface VideoVectorStoreMapper extends BaseMapper<VideoVectorStoreEntity> {

    @Select("SELECT * FROM video_vector_store WHERE video_id = #{videoId}")
    VideoVectorStoreEntity selectByVideoId(@Param("videoId") Long videoId);

    @Select("SELECT COUNT(*) FROM video_vector_store")
    int countAll();

    @Select("SELECT COUNT(*) FROM video_vector_store WHERE vehicle_id = #{vehicleId}")
    int countByVehicleId(@Param("vehicleId") Long vehicleId);

    @Select("SELECT COUNT(*) FROM video_vector_store WHERE function_domain_id = #{domainId}")
    int countByFunctionDomainId(@Param("domainId") Long domainId);

    @Select("SELECT * FROM video_vector_store WHERE vehicle_id = #{vehicleId} ORDER BY overall_score DESC LIMIT #{limit}")
    List<VideoVectorStoreEntity> selectTopByVehicleId(@Param("vehicleId") Long vehicleId, @Param("limit") int limit);

    @Select("SELECT * FROM video_vector_store WHERE function_domain_id = #{domainId} ORDER BY overall_score DESC LIMIT #{limit}")
    List<VideoVectorStoreEntity> selectTopByFunctionDomainId(@Param("domainId") Long domainId,
            @Param("limit") int limit);

    @Select("SELECT * FROM video_vector_store WHERE video_type = #{videoType} ORDER BY overall_score DESC LIMIT #{limit}")
    List<VideoVectorStoreEntity> selectTopByVideoType(@Param("videoType") Integer videoType, @Param("limit") int limit);

    @Select("SELECT * FROM video_vector_stats")
    VideoVectorStats selectStats();

    record VideoVectorStats(
            long totalVideos,
            long totalVehicles,
            long totalDomains,
            long goodCaseCount,
            long badCaseCount,
            double avgScore,
            Date lastUpdated) {
    }
}
