package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.QaQueryHistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询历史DAO接口
 */
@Mapper
public interface QaQueryHistoryDao extends BaseMapper<QaQueryHistoryEntity> {

    /**
     * 根据会话ID获取查询历史
     */
    @Select("SELECT * FROM qa_query_history WHERE session_id = #{sessionId} ORDER BY create_time DESC")
    List<QaQueryHistoryEntity> selectBySessionId(@Param("sessionId") String sessionId);

    /**
     * 获取用户最近的查询历史
     */
    @Select("SELECT * FROM qa_query_history WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<QaQueryHistoryEntity> selectRecentByUserId(@Param("userId") String userId, @Param("limit") int limit);

    /**
     * 获取时间范围内的查询统计
     */
    @Select("SELECT COUNT(*) FROM qa_query_history WHERE create_time BETWEEN #{startTime} AND #{endTime}")
    Long countByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取用户查询统计
     */
    @Select("SELECT COUNT(*) FROM qa_query_history WHERE user_id = #{userId} AND status = 'success'")
    Long countSuccessByUserId(@Param("userId") String userId);
}
