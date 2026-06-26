package com.xailab.vehicle.feign.vo;

import lombok.Data;

@Data
public class FunctionDomainVideoVo {

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频名称
     */
    private String videoUrl;

    /**
     * 视频封面
     */
    private String videoCoverUrl;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 视频任务类型
     */
    private String taskType;

    /**
     * 视频标题En
     */
    private String titleEn;

    /**
     * 视频描述En
     */
    private String descriptionEn;

    /**
     * 视频任务类型En
     */
    private String taskTypeEn;

    /**
     * 视频状态
     * 0：不可用，1：可用
     */
    private Integer status;
}
