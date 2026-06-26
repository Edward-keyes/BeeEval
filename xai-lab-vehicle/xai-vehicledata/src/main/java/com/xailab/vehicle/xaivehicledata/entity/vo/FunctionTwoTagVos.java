package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.xailab.vehicle.feign.vo.FunctionThreeTagVos;
import lombok.Data;

import java.util.List;

@Data
public class FunctionTwoTagVos {

    private Long twoTagId;
    /**
     * 标签编号
     */
    private String twoTagNumber;
    /**
     * 标签名称
     */
    private String twoTagName;
    /**
     * 三级list
     */
    private List<FunctionThreeTagVos> threeTagVos;
}
