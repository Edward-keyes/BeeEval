package com.xailab.vehicle.operation.beeeval.entity.vo;

import lombok.Data;

@Data
public class FunctionTreeSynchronizationVoData {

    /**
     * pcafe功能id
     */
    private String pcafeFunctionId;

    /**
     * pcafe功能名称
     */
    private String pcafeFunctionName;

    /**
     * beeeval三级标签编号
     */
    private String beeevalFunctionNumber;

    /**
     * beeeval三级标签名称
     */
    private String beeevalFunctionName;

}
