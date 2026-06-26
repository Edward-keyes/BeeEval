package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.VideoAnalysisResultEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VideoAnalysisResultDao extends BaseMapper<VideoAnalysisResultEntity> {

    @Select("SELECT * FROM video_analysis_result WHERE video_id = #{videoId}")
    VideoAnalysisResultEntity selectByVideoId(@Param("videoId") Long videoId);
}
