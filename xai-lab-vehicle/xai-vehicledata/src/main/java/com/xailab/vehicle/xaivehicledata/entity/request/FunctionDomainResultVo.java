package com.xailab.vehicle.xaivehicledata.entity.request;

import com.xailab.vehicle.xaivehicledata.entity.vo.ThreeData;
import lombok.Data;

import java.util.List;

@Data
public class FunctionDomainResultVo {
    /**
     * 一级标签id
     */
    private String oneId;
    /**
     * 一级标签名称
     */
    private String oneTagName;

    /**
     * 三级标签数据
     */
    private List<ThreeData> threeDatas;

//    public FunctionDomainResultVo(Long key, String value, List<ThreeData> value1) {
//        this.oneId = oneId;
//        this.oneTagName = oneTagName;
//        this.threeDatas = threeDatas;
//    }
}
