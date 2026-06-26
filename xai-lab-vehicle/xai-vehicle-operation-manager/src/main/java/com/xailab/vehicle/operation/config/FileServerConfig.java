package com.xailab.vehicle.operation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: FileServerConfig
 * @Description:
 * @author: liulin
 * @date: 2025/5/29 0:10
 */
@Component
@ConfigurationProperties(prefix = "pythonconfig.oss")
@Data
public class FileServerConfig {
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
