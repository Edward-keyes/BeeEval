package com.xailab.vehicle.xaivehicledata.entity.response;

import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalDomainIndicator;
import lombok.Data;

import java.util.List;

@Data
public class FunctionalDomainRepresentation {

    /**
     * 功能域ID
     */
    private String functionalDomainId;

    /**
     * 功能域名称
     */
    private String functionalDomainName;

    /**
     * 功能域总分
     */
    private double functionalDomainTotalScore;

    /**
     * 功能域指标
     */
    private List<FunctionalDomainIndicator> functionalDomainIndicators;
}
