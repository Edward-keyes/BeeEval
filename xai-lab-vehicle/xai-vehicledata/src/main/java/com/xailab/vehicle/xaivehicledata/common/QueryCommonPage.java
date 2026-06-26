/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xailab.vehicle.xaivehicledata.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xailab.vehicle.feign.common.PageQuery;
import com.xailab.vehicle.xaicommon.utils.Constant;
import com.xailab.vehicle.xaicommon.xss.SQLFilter;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 查询参数
 *
 *
 */
public class QueryCommonPage<T> {

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(Constant.PAGE) != null){
            curPage = Long.parseLong((String)params.get(Constant.PAGE));
        }
        if(params.get(Constant.LIMIT) != null){
            limit = Long.parseLong((String)params.get(Constant.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String)params.get(Constant.ORDER_FIELD));
        String order = (String)params.get(Constant.ORDER);


        //前端字段排序
        if(StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)){
            if(Constant.ASC.equalsIgnoreCase(order)) {
                return  page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if(StringUtils.isBlank(defaultOrderField)){
            return page;
        }

        //默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        }else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }



    public IPage<T> getPage(PageQuery pageQuery) {
        return this.getPage(pageQuery, null, false);
    }

    public IPage<T> getPage(PageQuery pageQuery, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(Objects.nonNull(pageQuery.getPage())){
            curPage = pageQuery.getPage();
        }
        if(Objects.nonNull(pageQuery.getLimit())){
            limit = pageQuery.getLimit();
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);


        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject(pageQuery.getOrder());


        //前端字段排序
        if(StringUtils.isNotBlank(orderField)){
            if(pageQuery.isAsc()) {
                return  page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }else {
            if(StringUtils.isNotBlank(defaultOrderField)){
                if(isAsc) {
                    return  page.addOrder(OrderItem.asc(defaultOrderField));
                }else {
                    return page.addOrder(OrderItem.desc(defaultOrderField));
                }
            }
        }
        return page;
    }
}
