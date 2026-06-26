package com.xailab.vehicle.xaimessage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@ConfigurationProperties(prefix="captcha.email")
@Data
public class EmailConfig {

    /**
     * 邮箱地址
     */
    private String user;
    /**
     * 发件人昵称
     */
    private String from;
    /**
     * 邮件服务器的SMTP地址
     */
    private String host;

    /**
     * 密码（授权码）
     */
    private String password;

}
