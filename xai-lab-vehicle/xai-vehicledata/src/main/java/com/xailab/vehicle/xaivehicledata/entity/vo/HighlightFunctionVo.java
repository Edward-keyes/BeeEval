package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class HighlightFunctionVo {

    private String brandModel;

    private String vehicleId;

    private String oneTagName;

    private String twoTagName;

    private String threeTagName;

    private String videoNumber;

    private String tagIcon;

    private Integer status;

    private String srtUrl;

//    public HighlightFunctionVo(String brandModel,String tagIcon, String oneTagName, String twoTagName, String threeTagName, String videoNumber) {
//        this.brandModel = brandModel;
//        this.oneTagName = oneTagName;
//        this.twoTagName = twoTagName;
//        this.threeTagName = threeTagName;
//        this.videoNumber = videoNumber;
//        this.tagIcon = tagIcon;
//    }

}
