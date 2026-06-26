package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.QaCacheEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 缓存DAO接口
 */
@Mapper
public interface QaCacheDao extends BaseMapper<QaCacheEntity> {

    /**
     * 根据缓存键获取缓存
     */
    @Select("SELECT * FROM qa_cache WHERE cache_key = #{cacheKey} AND expire_time > NOW()")
    QaCacheEntity selectByCacheKey(@Param("cacheKey") String cacheKey);

    /**
     * 根据问题哈希获取缓存
     */
    @Select("SELECT * FROM qa_cache WHERE question_hash = #{questionHash} AND expire_time > NOW() ORDER BY hit_count DESC")
    List<QaCacheEntity> selectByQuestionHash(@Param("questionHash") String questionHash);

    /**
     * 获取过期缓存
     */
    @Select("SELECT * FROM qa_cache WHERE expire_time <= NOW() LIMIT #{limit}")
    List<QaCacheEntity> selectExpiredCache(@Param("limit") int limit);

    /**
     * 删除过期缓存
     */
    @Delete("DELETE FROM qa_cache WHERE expire_time <= NOW()")
    int deleteExpiredCache();

    /**
     * 更新缓存命中次数
     */
    @Select("UPDATE qa_cache SET hit_count = hit_count + 1, last_access_time = NOW() WHERE id = #{id}")
    void incrementHitCount(@Param("id") Long id);

    /**
     * 获取热门缓存（命中率高的）
     */
    @Select("SELECT * FROM qa_cache WHERE expire_time > NOW() ORDER BY hit_count DESC LIMIT #{limit}")
    List<QaCacheEntity> selectPopularCache(@Param("limit") int limit);

    /**
     * 获取缓存统计信息
     */
    @Select("SELECT COUNT(*) as total_count, COUNT(CASE WHEN expire_time > NOW() THEN 1 END) as valid_count, SUM(hit_count) as total_hits FROM qa_cache")
    QaCacheEntity selectCacheStats();
}
