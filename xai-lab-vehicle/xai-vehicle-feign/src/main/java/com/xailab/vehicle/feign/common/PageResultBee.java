package com.xailab.vehicle.feign.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *

 */
@Data
public class PageResultBee<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 列表数据
     */
    private List<T> list;

    /**
     * 分页
     * @param list   列表数据
     * @param total  总记录数
     */
    public PageResultBee(List<T> list, long total) {
        this.list = list;
        this.total = (int)total;
    }
}