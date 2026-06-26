package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;

@Data
public class FunctionRichnessRatioRequest {

    /**
     * 功能场景名称
     */
    private String functionName;

    /**
     * 分子
     */
    private String molecule;

    /**
     * 分母
     */
    private String denominator;

    /**
     * 占比
     */
    private String ratio;

}
