package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆同步分数表
 * 存储从数据管理平台同步过来的分数数据
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Data
@TableName("vehicle_sync_score")
public class VehicleSyncScoreEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * BeeEval车辆基础信息ID
     */
    private Long vehicleId;

    /**
     * 功能域ID
     */
    private Long functionalDomainId;

    /**
     * 三级指标ID（可为空）
     */
    private Long domainIndexId;

    /**
     * 分数值
     */
    private BigDecimal scoreValue;

    /**
     * 分数单位（如：分、%、秒等）
     */
    private String scoreUnit;

    /**
     * 同步类型：1-功能域分数, 2-三级指标分数, 3-基础能力分数
     */
    private Integer syncType;

    /**
     * 数据管理平台测试记录ID
     */
    private Integer testRecordId;

    /**
     * 同步批次号（用于标识一次批量同步操作）
     */
    private String syncBatchId;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 同步时间
     */
    private Date syncTime;

    /**
     * 状态：0-无效，1-有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
