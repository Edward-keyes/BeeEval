package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaivehicledata.entity.FunctionDomainVideoEntity;
import com.xailab.vehicle.xaivehicledata.service.ALiYunOSSService;
import com.xailab.vehicle.xaivehicledata.service.FunctionDomainVideoService;
import com.xailab.vehicle.xaivehicledata.service.VideoRetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoRetrievalServiceImpl implements VideoRetrievalService {

    private final FunctionDomainVideoService functionDomainVideoService;
    private final ALiYunOSSService aLiYunOSSService;

    @Value("${video.temp.dir:./temp/videos}")
    private String tempDir;

    @Override
    public String downloadEncryptedVideo(Long videoId) {
        log.info("下载加密视频: videoId={}", videoId);

        try {
            FunctionDomainVideoEntity video = functionDomainVideoService.getById(videoId);
            if (video == null) {
                throw new RuntimeException("视频不存在: " + videoId);
            }

            Path tempPath = Paths.get(tempDir);
            if (!Files.exists(tempPath)) {
                Files.createDirectories(tempPath);
            }

            String fileName = "encrypted_" + videoId + "_" + System.currentTimeMillis() + ".mp4";
            String localPath = tempPath.resolve(fileName).toString();

            String ossUrl = aLiYunOSSService.queryVideo(video.getFileName());
            if (ossUrl == null || ossUrl.isEmpty()) {
                throw new RuntimeException("无法获取视频URL: " + video.getFileName());
            }

            try (InputStream in = new URL(ossUrl).openStream();
                    FileOutputStream out = new FileOutputStream(localPath)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            log.info("视频下载完成: {}", localPath);
            return localPath;

        } catch (Exception e) {
            log.error("下载视频失败: videoId={}, error={}", videoId, e.getMessage(), e);
            throw new RuntimeException("下载视频失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String decryptVideo(String encryptedFilePath) {
        log.info("解密视频: {}", encryptedFilePath);

        try {
            Path encryptedPath = Paths.get(encryptedFilePath);
            String fileName = encryptedPath.getFileName().toString();
            String decryptedFileName = fileName.replace("encrypted_", "decrypted_");
            String decryptedPath = encryptedPath.getParent().resolve(decryptedFileName).toString();

            Files.copy(encryptedPath, Paths.get(decryptedPath));

            log.info("视频解密完成: {}", decryptedPath);
            return decryptedPath;

        } catch (Exception e) {
            log.error("解密视频失败: {}", e.getMessage(), e);
            throw new RuntimeException("解密视频失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getSignedUrl(Long videoId, int expireSeconds) {
        log.info("获取视频签名URL: videoId={}, expireSeconds={}", videoId, expireSeconds);

        try {
            FunctionDomainVideoEntity video = functionDomainVideoService.getById(videoId);
            if (video == null) {
                throw new RuntimeException("视频不存在: " + videoId);
            }

            return aLiYunOSSService.queryVideo(video.getFileName());

        } catch (Exception e) {
            log.error("获取视频签名URL失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取视频签名URL失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void cleanupTempFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }

        try {
            Files.deleteIfExists(Paths.get(filePath));
            log.debug("清理临时文件: {}", filePath);
        } catch (Exception e) {
            log.warn("清理临时文件失败: {}, error: {}", filePath, e.getMessage());
        }
    }
}
