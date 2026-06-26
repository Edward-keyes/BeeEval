package com.xailab.vehicle.operation.testplatform.service;

import com.alibaba.nacos.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // 获取项目基础路径
    private final Path rootLocation = Paths.get(System.getProperty("user.dir"), "uploads");

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("无法创建上传目录", e);
        }
    }

    public Path store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new Exception("空文件: " + file.getOriginalFilename());
            }

            // 生成安全文件名
            String filename = StringUtils.cleanPath(file.getOriginalFilename()
            );

            // 解析保存路径 (项目目录/uploads/)
            Path destPath = rootLocation.resolve(filename);

            // 防止路径攻击
            if (filename.contains("..")) {
                throw new SecurityException(
                        "文件名包含非法路径序列: " + filename);
            }

            // 保存文件到本地
            Files.copy(file.getInputStream(), destPath,
                    StandardCopyOption.REPLACE_EXISTING);

            return destPath; // 返回保存后的绝对路径
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}