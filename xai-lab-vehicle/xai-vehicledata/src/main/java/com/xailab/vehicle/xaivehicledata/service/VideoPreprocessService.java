package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.dto.VideoMetadata;

import java.util.List;

public interface VideoPreprocessService {

    String extractAudio(String videoPath);

    List<String> captureFrames(String videoPath, List<Double> timestamps);

    String extractSubtitle(String videoPath);

    VideoMetadata getVideoMetadata(String videoPath);

    void cleanupTempFiles(List<String> filePaths);
}
