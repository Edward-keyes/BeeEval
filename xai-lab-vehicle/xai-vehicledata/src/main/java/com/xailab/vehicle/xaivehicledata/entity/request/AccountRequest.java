package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

/**
 * 
 */
@Data
public class AccountRequest {

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

}
