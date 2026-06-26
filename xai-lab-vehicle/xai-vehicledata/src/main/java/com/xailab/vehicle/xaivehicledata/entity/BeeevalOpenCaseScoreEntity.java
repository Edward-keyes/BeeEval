package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 开源用例分数表
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Data
@TableName("beeeval_open_case_score")
public class BeeevalOpenCaseScoreEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 车辆ID
     */
    private Long vehicleId;

    /**
     * 三级指标ID
     */
    private Long threeTagId;

    /**
     * 开源用例ID
     */
    private Integer caseId;

    /**
     * 分数
     */
    private Integer score;
}
