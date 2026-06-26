package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

@Data
public class FunctionalVideoNewVo {

    private String id;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 名称
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 详情
     */
    private String description;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 三级标签
     */
    private String ThreeTag;

    /**
     * 功能域
     */
    private String functionalDomain;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 字幕
     */
    private String srtUrl;

}
