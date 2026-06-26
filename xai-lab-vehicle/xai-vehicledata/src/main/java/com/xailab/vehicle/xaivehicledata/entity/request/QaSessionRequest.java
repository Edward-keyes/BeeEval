package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 会话请求DTO
 */
@Data
public class QaSessionRequest {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;
}
