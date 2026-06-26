package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class FunctionTwoTagVo {

    /**
     * 二级标签名称
     */
    private String twoTagName;

    /**
     * 三级标签列表
     */
    private List<FunctionThreeTagVo> functionThreeTagVOList;

}
