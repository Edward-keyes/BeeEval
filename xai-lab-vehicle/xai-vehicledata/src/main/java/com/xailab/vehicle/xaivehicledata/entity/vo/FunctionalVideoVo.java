package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionalVideoVo {

    private String id;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 视频封面
     */
    private String videoCover;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 字幕
     */
    private String srtUrl;

    /**
     * 标题
     */
    private String title;

}
