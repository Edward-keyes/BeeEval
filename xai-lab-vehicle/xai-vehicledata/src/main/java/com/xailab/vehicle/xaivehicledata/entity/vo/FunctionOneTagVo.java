package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class FunctionOneTagVo {

    /**
     * 一级标签名称
     */
    private String oneTagName;

    /**
     * 二级标签列表
     */
    private List<FunctionTwoTagVo> functionTwoTagVOList;

}
