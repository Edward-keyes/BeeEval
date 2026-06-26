package com.xailab.vehicle.feign.pojo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeListResponse
 * @Description:
 * @author: liulin
 * @date: 2025/6/8 1:24
 */
@Data
public class FunctionTreeListResponse implements Serializable {

    /**
     * 标签编号
     */
    private String tagNumber;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 说明
     */
    private String description;

    /**
     * 功能树用例
     */
    private List<FunctionTreeCaseInfoResponse> functionTreeCase;

}
