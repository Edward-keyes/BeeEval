package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("vehicle_function_domain_video")
public class FunctionDomainVideoEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 功能域id
     */
    private Long functionDomainId;

    /**
     * 功能域三级指标id
     */
    private Long functionDomainIndexId;

    /**
     * 视频类型 '0=bad,1=good'
     */
    private Integer type;

    /**
     * 视频名称
     */
    private String urlName;

    /**
     * 文件类型 1=mp4，2=srt
     */
    private Integer fileType;

    /**
     * 是否有字幕 1=有，0 or null =没有
     */
    private Integer isSrt;

    /**
     * 标题中文
     */
    private String title;

    /**
     * 任务类型中文
     */
    private String taskType;

    /**
     * 任务类型英文
     */
    private String taskTypeEn;

    /**
     * 标题英文
     */
    private String titleEn;

    /**
     * 详情中文
     */
    private String description;

    /**
     * 详情英文
     */
    private String descriptionEn;

    /**
     * 视频名称
     */
    private String fileName;

    /**
     * 0：不可用，1：可用
     */
    private Integer status;
}
