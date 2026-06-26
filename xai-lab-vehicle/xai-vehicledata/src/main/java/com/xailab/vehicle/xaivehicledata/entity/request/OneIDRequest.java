package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

@Data
public class OneIDRequest {

    private String id;

    private String language;

    /**
     * 分页参数：页码，从1开始
     * 第一页返回5条视频，后续每页返回2条视频
     */
    private Integer page = 1;

}
