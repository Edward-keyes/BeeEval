package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: DomainTreeQueryScoreRequest
 * @Description:
 *
 * @date: 2025/3/1 21:55
 */
@Data
public class DomainTreeQueryScoreRequest implements Serializable {

    /**
     * 功能域
     */
    private Long functionalDomain;

    /**
     * 车辆基础信息 id
     */
    private List<Long> brandBaseInfoId;

    private String language;
}
