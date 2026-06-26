package com.xailab.operation.api.feign.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: OSSResult
 * @Description:
 * @author: liulin
 * @date: 2025/6/10 10:59
 */
@Data
public class OSSResult<T> implements Serializable {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String error;


}
