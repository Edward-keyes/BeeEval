package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FunctionalDomainIndexAvgScoreVo
 * @Description:
 * 
 * @date: 2025/3/1 22:53
 */
@Data
public class DomainIndexAvgScoreVo implements Serializable {

    private String doMainIndexId;
    /**
     * 指标名称
     */
    private String doMainIndexName;
    /**
     * 指标平均分
     */
    private Double doMainIndexAvgScore;
    /**
     * 指标详情
     */
    private String indexDetail;

}
