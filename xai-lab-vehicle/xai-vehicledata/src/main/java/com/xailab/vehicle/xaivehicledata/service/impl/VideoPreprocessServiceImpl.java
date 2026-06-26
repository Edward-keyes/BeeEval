package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaivehicledata.entity.dto.VideoMetadata;
import com.xailab.vehicle.xaivehicledata.service.VideoPreprocessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class VideoPreprocessServiceImpl implements VideoPreprocessService {

    @Value("${video.temp.dir:./temp/videos}")
    private String tempDir;

    @Value("${ffmpeg.path:ffmpeg}")
    private String ffmpegPath;

    @Override
    public String extractAudio(String videoPath) {
        log.info("提取音频: videoPath={}", videoPath);

        try {
            Path tempPath = Paths.get(tempDir);
            if (!Files.exists(tempPath)) {
                Files.createDirectories(tempPath);
            }

            String audioFileName = "audio_" + System.currentTimeMillis() + ".mp3";
            String audioPath = tempPath.resolve(audioFileName).toString();

            ProcessBuilder pb = new ProcessBuilder(
                    ffmpegPath,
                    "-i", videoPath,
                    "-vn",
                    "-acodec", "libmp3lame",
                    "-q:a", "2",
                    "-y",
                    audioPath);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder errorOutput = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
                throw new RuntimeException("FFmpeg音频提取失败: " + errorOutput);
            }

            log.info("音频提取完成: {}", audioPath);
            return audioPath;

        } catch (Exception e) {
            log.error("提取音频失败: {}", e.getMessage(), e);
            throw new RuntimeException("提取音频失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> captureFrames(String videoPath, List<Double> timestamps) {
        log.info("截取关键帧: videoPath={}, timestamps={}", videoPath, timestamps);

        List<String> framePaths = new ArrayList<>();

        try {
            Path tempPath = Paths.get(tempDir);
            if (!Files.exists(tempPath)) {
                Files.createDirectories(tempPath);
            }

            VideoMetadata metadata = getVideoMetadata(videoPath);
            double duration = metadata.getDuration() != null ? metadata.getDuration() / 1000.0 : 0;

            for (int i = 0; i < timestamps.size(); i++) {
                Double timestamp = timestamps.get(i);
                double actualTime = duration * timestamp;

                String frameFileName = "frame_" + System.currentTimeMillis() + "_" + i + ".jpg";
                String framePath = tempPath.resolve(frameFileName).toString();

                ProcessBuilder pb = new ProcessBuilder(
                        ffmpegPath,
                        "-ss", String.valueOf(actualTime),
                        "-i", videoPath,
                        "-vframes", "1",
                        "-q:v", "2",
                        "-y",
                        framePath);
                pb.redirectErrorStream(true);

                Process process = pb.start();
                int exitCode = process.waitFor();

                if (exitCode != 0) {
                    log.warn("截取关键帧失败: timestamp={}", actualTime);
                    continue;
                }

                framePaths.add(framePath);
                log.debug("关键帧截取完成: {}", framePath);
            }

            log.info("关键帧截取完成: count={}", framePaths.size());
            return framePaths;

        } catch (Exception e) {
            log.error("截取关键帧失败: {}", e.getMessage(), e);
            throw new RuntimeException("截取关键帧失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String extractSubtitle(String videoPath) {
        log.info("提取字幕: videoPath={}", videoPath);

        try {
            Path tempPath = Paths.get(tempDir);
            if (!Files.exists(tempPath)) {
                Files.createDirectories(tempPath);
            }

            String subtitleFileName = "subtitle_" + System.currentTimeMillis() + ".srt";
            String subtitlePath = tempPath.resolve(subtitleFileName).toString();

            ProcessBuilder pb = new ProcessBuilder(
                    ffmpegPath,
                    "-i", videoPath,
                    "-map", "0:s:0",
                    "-y",
                    subtitlePath);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                log.info("视频没有字幕或提取失败");
                return null;
            }

            String subtitle = new String(Files.readAllBytes(Paths.get(subtitlePath)));
            Files.deleteIfExists(Paths.get(subtitlePath));

            log.info("字幕提取完成，长度: {}", subtitle.length());
            return subtitle;

        } catch (Exception e) {
            log.info("提取字幕失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public VideoMetadata getVideoMetadata(String videoPath) {
        log.info("获取视频元数据: videoPath={}", videoPath);

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    ffmpegPath,
                    "-i", videoPath);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();

            String metadataStr = output.toString();
            return parseMetadata(metadataStr);

        } catch (Exception e) {
            log.error("获取视频元数据失败: {}", e.getMessage(), e);
            return VideoMetadata.builder().build();
        }
    }

    @Override
    public void cleanupTempFiles(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            return;
        }

        for (String filePath : filePaths) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
                log.debug("清理临时文件: {}", filePath);
            } catch (Exception e) {
                log.warn("清理临时文件失败: {}, error: {}", filePath, e.getMessage());
            }
        }
    }

    private VideoMetadata parseMetadata(String metadataStr) {
        VideoMetadata.VideoMetadataBuilder builder = VideoMetadata.builder();

        Pattern durationPattern = Pattern.compile("Duration: (\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{2})");
        Matcher durationMatcher = durationPattern.matcher(metadataStr);
        if (durationMatcher.find()) {
            int hours = Integer.parseInt(durationMatcher.group(1));
            int minutes = Integer.parseInt(durationMatcher.group(2));
            int seconds = Integer.parseInt(durationMatcher.group(3));
            long durationMs = (hours * 3600 + minutes * 60 + seconds) * 1000L;
            builder.duration(durationMs);
        }

        Pattern resolutionPattern = Pattern.compile("(\\d{2,5})x(\\d{2,5})");
        Matcher resolutionMatcher = resolutionPattern.matcher(metadataStr);
        if (resolutionMatcher.find()) {
            builder.width(Integer.parseInt(resolutionMatcher.group(1)));
            builder.height(Integer.parseInt(resolutionMatcher.group(2)));
        }

        Pattern fpsPattern = Pattern.compile("(\\d+\\.?\\d*) fps");
        Matcher fpsMatcher = fpsPattern.matcher(metadataStr);
        if (fpsMatcher.find()) {
            builder.frameRate(Double.parseDouble(fpsMatcher.group(1)));
        }

        Pattern bitratePattern = Pattern.compile("(\\d+) kb/s");
        Matcher bitrateMatcher = bitratePattern.matcher(metadataStr);
        if (bitrateMatcher.find()) {
            builder.bitrate(Long.parseLong(bitrateMatcher.group(1)) * 1000);
        }

        builder.hasAudio(metadataStr.contains("Audio:"));
        builder.hasSubtitle(metadataStr.contains("Subtitle:"));

        return builder.build();
    }
}
