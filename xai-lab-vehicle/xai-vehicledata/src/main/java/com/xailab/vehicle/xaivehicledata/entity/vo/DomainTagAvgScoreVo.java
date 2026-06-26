package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: DomainTagAvgScoreVo
 * @Description:
 *
 * @date: 2025/3/2 17:39
 */
@Data
public class DomainTagAvgScoreVo implements Serializable {

    /**
     * 域标签tag
     */
    private String domainTagId;

    /**
     * 域tag name
     */
    private String domainTagName;

    /**
     * 平均分
     */
    private Double avgScore;

}
