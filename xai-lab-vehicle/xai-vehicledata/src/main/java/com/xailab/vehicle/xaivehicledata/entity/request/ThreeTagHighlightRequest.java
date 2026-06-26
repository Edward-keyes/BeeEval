package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

@Data
public class ThreeTagHighlightRequest {

    /**
     * 三级标签名称
     */
    private String threeTagName;

    /**
     * 视频号
     */
    private String videoUrl;

    /**
     * 汽车品牌型号
     */
    private String brandName;

    /**
     * 车辆ID
     */
    private String vehicleId;

    /**
     * 车辆状态
     */
    private Integer status;

    /**
     * 字幕地址
     */
    private String srtUrl;



    public ThreeTagHighlightRequest(String threeTagName, String videoNumber, String brandModel ,String vehicleId,Integer status,String srtUrl) {
        this.threeTagName = threeTagName;
        this.videoUrl = videoNumber;
        this.brandName = brandModel;
        this.vehicleId = vehicleId;
        this.status = status;
        this.srtUrl = srtUrl;
    }

    @Override
    public String toString() {
        return "ThreeTagHighlightRequest{" +
                "threeTagName='" + threeTagName + '\'' +
                ", videoNumber='" + videoUrl + '\'' +
                ", brandModel='" + brandName + '\'' +
                '}';
    }

}
