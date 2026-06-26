package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

@Data
public class FunctionCaseData {

    /**
     * 用例
     */
    private String caseContent;

    /**
     * 用例说明
     */
    private String[] functionLabel;

    /**
     * 视频编号
     */
    private String videoNumber;

    /**
     * 视频字幕
     */
    private String videoStr;

    /**
     * 类型
     */
    private Integer materialType;

}
