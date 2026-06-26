package com.xailab.vehicle.operation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class ALiYunOssConfig {

    private String bucketName;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String subtitle;

}
