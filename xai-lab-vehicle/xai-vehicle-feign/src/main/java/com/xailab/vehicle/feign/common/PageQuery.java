package com.xailab.vehicle.feign.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询公共参数
 */
@Data
public class PageQuery implements Serializable {
    /**
     * 页码不能为空
     * 页码最小值为 1
     */
    Integer page;

    /**
     * 每页条数不能为空
     * 每页条数，取值范围 1-1000
     */
    Integer limit;

    /**
     * 排序字段
     */
    String order;

    /**
     * 是否升序
     */
    boolean asc;
}
