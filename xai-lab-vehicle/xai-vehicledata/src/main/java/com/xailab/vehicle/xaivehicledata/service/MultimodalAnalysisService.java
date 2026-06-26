package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.dto.TranscriptResult;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisRequest;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisResult;

public interface MultimodalAnalysisService {

    VideoAnalysisResult analyzeVideo(VideoAnalysisRequest request);

    TranscriptResult transcribeAudio(String audioPath);

    String understandImage(String imagePath, String question);

    String analyzeWithMultimodal(String text, String imageUrl);
}
