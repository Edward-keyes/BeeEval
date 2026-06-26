package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.QaAuditLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询审计DAO接口
 */
@Mapper
public interface QaAuditLogDao extends BaseMapper<QaAuditLogEntity> {

    /**
     * 根据用户ID获取审计日志
     */
    @Select("SELECT * FROM qa_audit_log WHERE user_id = #{userId} ORDER BY access_time DESC")
    List<QaAuditLogEntity> selectByUserId(@Param("userId") String userId);

    /**
     * 获取时间范围内的审计日志
     */
    @Select("SELECT * FROM qa_audit_log WHERE access_time BETWEEN #{startTime} AND #{endTime} ORDER BY access_time DESC")
    List<QaAuditLogEntity> selectByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取敏感数据访问记录
     */
    @Select("SELECT * FROM qa_audit_log WHERE is_sensitive = true ORDER BY access_time DESC")
    List<QaAuditLogEntity> selectSensitiveAccess();

    /**
     * 统计用户访问次数
     */
    @Select("SELECT COUNT(*) FROM qa_audit_log WHERE user_id = #{userId} AND access_time >= #{startTime}")
    Long countUserAccess(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime);

    /**
     * 统计IP访问次数
     */
    @Select("SELECT COUNT(*) FROM qa_audit_log WHERE ip_address = #{ipAddress} AND access_time >= #{startTime}")
    Long countIpAccess(@Param("ipAddress") String ipAddress, @Param("startTime") LocalDateTime startTime);
}
