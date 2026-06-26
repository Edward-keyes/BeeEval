package com.xailab.vehicle.xaivehicledata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pythonconfig.oss")
@Data
public class ALiYunOssConfig {

    private String saveFileToOSS;

    private String getFileStatus;

    private String getPlayList;

    private String getImageUrl;

    private String getFiles;

    private String deleteFile;

    private String deleteFiles;

    private String bucketName;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String subtitle;
}
