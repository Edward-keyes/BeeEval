package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xailab.vehicle.xaivehicledata.config.QaLlmConfig;
import com.xailab.vehicle.xaivehicledata.entity.dto.TranscriptResult;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisRequest;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisResult;
import com.xailab.vehicle.xaivehicledata.service.MultimodalAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MultimodalAnalysisServiceImpl implements MultimodalAnalysisService {

    private final QaLlmConfig llmConfig;

    @Override
    public VideoAnalysisResult analyzeVideo(VideoAnalysisRequest request) {
        log.info("开始分析视频: videoId={}", request.getVideoId());
        long startTime = System.currentTimeMillis();

        try {
            String prompt = buildVideoAnalysisPrompt(request);
            log.debug("视频分析Prompt: {}", prompt);

            String response = callMultimodalApi(prompt, request.getFramePaths());
            log.info("通义千问多模态API返回: {}", response);

            VideoAnalysisResult result = parseAnalysisResult(response, request.getVideoId());
            result.setProcessingTimeMs((int) (System.currentTimeMillis() - startTime));
            result.setTranscript(request.getTranscript());

            return result;
        } catch (Exception e) {
            log.error("视频分析失败: videoId={}, error={}", request.getVideoId(), e.getMessage(), e);
            return VideoAnalysisResult.builder()
                    .videoId(request.getVideoId())
                    .summary("分析失败: " + e.getMessage())
                    .processingTimeMs((int) (System.currentTimeMillis() - startTime))
                    .build();
        }
    }

    @Override
    public TranscriptResult transcribeAudio(String audioPath) {
        log.info("开始语音转文字: audioPath={}", audioPath);
        long startTime = System.currentTimeMillis();

        try {
            String prompt = buildTranscriptionPrompt();
            String response = callAudioApi(prompt, audioPath);

            TranscriptResult result = parseTranscriptResult(response);
            log.info("语音转文字完成，耗时: {}ms", System.currentTimeMillis() - startTime);

            return result;
        } catch (Exception e) {
            log.error("语音转文字失败: {}", e.getMessage(), e);
            return TranscriptResult.builder()
                    .text("")
                    .confidence(0.0)
                    .build();
        }
    }

    @Override
    public String understandImage(String imagePath, String question) {
        log.info("开始图像理解: imagePath={}", imagePath);

        try {
            return callMultimodalApi(question, Arrays.asList(imagePath));
        } catch (Exception e) {
            log.error("图像理解失败: {}", e.getMessage(), e);
            return "图像理解失败: " + e.getMessage();
        }
    }

    @Override
    public String analyzeWithMultimodal(String text, String imageUrl) {
        log.info("开始多模态分析");

        try {
            return callMultimodalApi(text, Arrays.asList(imageUrl));
        } catch (Exception e) {
            log.error("多模态分析失败: {}", e.getMessage(), e);
            return "分析失败: " + e.getMessage();
        }
    }

    private String buildVideoAnalysisPrompt(VideoAnalysisRequest request) {
        return String.format("""
                你是一个专业的汽车智能驾驶评估专家。请基于以下信息对视频进行分析：

                ## 视频基本信息
                - 车辆: %s
                - 功能域: %s
                - 三级指标: %s
                - 视频类型: %s (good=表现好，bad=表现不好)
                - 任务类型: %s
                - 描述: %s

                ## 音频转录内容
                %s

                ## 视觉内容
                请结合提供的视频截图进行分析

                ## 评估要求
                请从以下维度进行评估：
                1. 功能表现 (0-100分)
                2. 响应速度 (0-100分)
                3. 用户体验 (0-100分)
                4. 安全性 (0-100分)
                5. 稳定性 (0-100分)

                ## 输出格式
                请严格按照以下JSON格式返回，不要包含其他文字：
                {
                  "overall_score": 85,
                  "dimensions": {
                    "functionality": {"score": 88, "comment": "功能表现评价"},
                    "response_speed": {"score": 82, "comment": "响应速度评价"},
                    "user_experience": {"score": 85, "comment": "用户体验评价"},
                    "safety": {"score": 90, "comment": "安全性评价"},
                    "stability": {"score": 80, "comment": "稳定性评价"}
                  },
                  "summary": "总体评价",
                  "improvement_suggestions": ["建议1", "建议2"],
                  "key_observations": ["关键发现1", "关键发现2"],
                  "visual_description": "视觉内容描述"
                }
                """,
                request.getVehicleName(),
                request.getDomainName(),
                request.getIndexName(),
                request.getVideoType(),
                request.getTaskType(),
                request.getDescription(),
                request.getTranscript() != null ? request.getTranscript() : "无音频转录");
    }

    private String buildTranscriptionPrompt() {
        return """
                请将音频内容转换为文字。要求：
                1. 准确识别语音内容
                2. 添加适当的标点符号
                3. 保持原始语序
                4. 如果有多个说话人，请标注

                返回JSON格式：
                {
                  "text": "完整的转录文本",
                  "confidence": 0.95,
                  "language": "zh",
                  "segments": [
                    {"start_time": 0, "end_time": 5, "text": "片段文本", "confidence": 0.98}
                  ]
                }
                """;
    }

    private String callMultimodalApi(String text, List<String> imagePaths) throws Exception {
        List<Object> content = new ArrayList<>();
        content.add(Map.of("type", "text", "text", text));

        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String imagePath : imagePaths) {
                String base64Image = encodeImageToBase64(imagePath);
                String imageUrl = "data:image/jpeg;base64," + base64Image;
                content.add(Map.of("type", "image_url", "image_url", Map.of("url", imageUrl)));
            }
        }

        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(content.toString())
                .build();

        GenerationParam param = GenerationParam.builder()
                .apiKey(llmConfig.getApiKey())
                .model("qwen-vl-max")
                .messages(List.of(userMessage))
                .maxTokens(2000)
                .build();

        Generation gen = new Generation();
        GenerationResult result = gen.call(param);

        if (result != null && result.getOutput() != null
                && result.getOutput().getChoices() != null
                && !result.getOutput().getChoices().isEmpty()) {
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        }

        return "";
    }

    private String callAudioApi(String prompt, String audioPath) throws Exception {
        String base64Audio = encodeAudioToBase64(audioPath);
        String audioUrl = "data:audio/mp3;base64," + base64Audio;

        List<Object> content = new ArrayList<>();
        content.add(Map.of("type", "text", "text", prompt));
        content.add(Map.of("type", "audio_url", "audio_url", Map.of("url", audioUrl)));

        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(content.toString())
                .build();

        GenerationParam param = GenerationParam.builder()
                .apiKey(llmConfig.getApiKey())
                .model("qwen-audio-turbo")
                .messages(List.of(userMessage))
                .maxTokens(2000)
                .build();

        Generation gen = new Generation();
        GenerationResult result = gen.call(param);

        if (result != null && result.getOutput() != null
                && result.getOutput().getChoices() != null
                && !result.getOutput().getChoices().isEmpty()) {
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        }

        return "";
    }

    private VideoAnalysisResult parseAnalysisResult(String response, Long videoId) {
        try {
            JSONObject json = JSON.parseObject(response);

            Map<String, VideoAnalysisResult.DimensionScore> dimensions = new HashMap<>();
            JSONObject dimensionsJson = json.getJSONObject("dimensions");
            if (dimensionsJson != null) {
                for (String key : dimensionsJson.keySet()) {
                    JSONObject dimJson = dimensionsJson.getJSONObject(key);
                    dimensions.put(key, VideoAnalysisResult.DimensionScore.builder()
                            .score(dimJson.getDouble("score"))
                            .comment(dimJson.getString("comment"))
                            .build());
                }
            }

            return VideoAnalysisResult.builder()
                    .videoId(videoId)
                    .overallScore(json.getDouble("overall_score"))
                    .dimensions(dimensions)
                    .summary(json.getString("summary"))
                    .improvementSuggestions(json.getJSONArray("improvement_suggestions").toJavaList(String.class))
                    .keyObservations(json.getJSONArray("key_observations").toJavaList(String.class))
                    .visualDescription(json.getString("visual_description"))
                    .build();
        } catch (Exception e) {
            log.error("解析分析结果失败: {}", e.getMessage(), e);
            return VideoAnalysisResult.builder()
                    .videoId(videoId)
                    .summary(response)
                    .build();
        }
    }

    private TranscriptResult parseTranscriptResult(String response) {
        try {
            JSONObject json = JSON.parseObject(response);

            List<TranscriptResult.Segment> segments = new ArrayList<>();
            if (json.containsKey("segments")) {
                for (Object obj : json.getJSONArray("segments")) {
                    JSONObject segJson = (JSONObject) obj;
                    segments.add(TranscriptResult.Segment.builder()
                            .startTime(segJson.getInteger("start_time"))
                            .endTime(segJson.getInteger("end_time"))
                            .text(segJson.getString("text"))
                            .confidence(segJson.getDouble("confidence"))
                            .build());
                }
            }

            return TranscriptResult.builder()
                    .text(json.getString("text"))
                    .confidence(json.getDouble("confidence"))
                    .language(json.getString("language"))
                    .segments(segments)
                    .build();
        } catch (Exception e) {
            log.error("解析转录结果失败: {}", e.getMessage(), e);
            return TranscriptResult.builder()
                    .text(response)
                    .confidence(0.8)
                    .build();
        }
    }

    private String encodeImageToBase64(String imagePath) throws Exception {
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private String encodeAudioToBase64(String audioPath) throws Exception {
        byte[] audioBytes = Files.readAllBytes(Paths.get(audioPath));
        return Base64.getEncoder().encodeToString(audioBytes);
    }
}
