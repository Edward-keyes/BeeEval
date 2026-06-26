package com.xailab.vehicle.xaivehicledata;

import cn.dev33.satoken.stp.StpUtil;
import com.xailab.vehicle.xaivehicledata.entity.FunctionDomainVideoEntity;
import com.xailab.vehicle.xaivehicledata.entity.dto.TranscriptResult;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisRequest;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoAnalysisResult;
import com.xailab.vehicle.xaivehicledata.entity.dto.VideoMetadata;
import com.xailab.vehicle.xaivehicledata.entity.request.QaQueryRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.QaSessionRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.QaQueryResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.QaSessionResponse;
import com.xailab.vehicle.xaivehicledata.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("qa")
public class VideoAnalysisTest {

    @Autowired
    private VideoRetrievalService videoRetrievalService;

    @Autowired
    private VideoPreprocessService videoPreprocessService;

    @Autowired
    private MultimodalAnalysisService multimodalAnalysisService;

    @Autowired
    private VideoAnalysisOrchestrationService videoAnalysisService;

    @Autowired
    private FunctionDomainVideoService functionDomainVideoService;

    @Autowired
    private QaSessionService qaSessionService;

    @Autowired
    private QaQueryService qaQueryService;

    @BeforeEach
    public void setUp() {
        log.info("测试开始前：模拟Sa-Token登录");
        StpUtil.login(1001L, "test_user");
    }

    @AfterEach
    public void tearDown() {
        log.info("测试结束后：退出Sa-Token登录");
        StpUtil.logout();
    }

    @Test
    public void testVideoMetadataExtraction() {
        log.info("测试视频元数据提取");

        String testVideoPath = getTestVideoPath();
        if (testVideoPath == null) {
            log.warn("测试视频不存在，跳过测试");
            return;
        }

        VideoMetadata metadata = videoPreprocessService.getVideoMetadata(testVideoPath);

        assertNotNull(metadata, "元数据不能为空");
        log.info("视频元数据: duration={}ms, width={}, height={}, hasAudio={}, hasSubtitle={}",
                metadata.getDuration(), metadata.getWidth(), metadata.getHeight(),
                metadata.getHasAudio(), metadata.getHasSubtitle());
    }

    @Test
    public void testAudioExtraction() {
        log.info("测试音频提取");

        String testVideoPath = getTestVideoPath();
        if (testVideoPath == null) {
            log.warn("测试视频不存在，跳过测试");
            return;
        }

        try {
            String audioPath = videoPreprocessService.extractAudio(testVideoPath);

            assertNotNull(audioPath, "音频路径不能为空");
            log.info("音频提取成功: {}", audioPath);

            videoPreprocessService.cleanupTempFiles(Arrays.asList(audioPath));
            log.info("临时音频文件已清理");

        } catch (Exception e) {
            log.error("音频提取测试失败: {}", e.getMessage(), e);
            fail("音频提取失败: " + e.getMessage());
        }
    }

    @Test
    public void testFrameCapture() {
        log.info("测试关键帧截取");

        String testVideoPath = getTestVideoPath();
        if (testVideoPath == null) {
            log.warn("测试视频不存在，跳过测试");
            return;
        }

        try {
            List<String> frames = videoPreprocessService.captureFrames(testVideoPath,
                    Arrays.asList(0.5));

            assertNotNull(frames, "帧列表不能为空");
            assertTrue(frames.size() > 0, "应该至少截取一帧");
            log.info("关键帧截取成功，数量: {}", frames.size());

            videoPreprocessService.cleanupTempFiles(frames);
            log.info("临时帧文件已清理");

        } catch (Exception e) {
            log.error("关键帧截取测试失败: {}", e.getMessage(), e);
            fail("关键帧截取失败: " + e.getMessage());
        }
    }

    @Test
    public void testAudioTranscription() {
        log.info("测试语音转文字");

        String testAudioPath = getTestAudioPath();
        if (testAudioPath == null) {
            log.warn("测试音频不存在，跳过测试");
            return;
        }

        try {
            TranscriptResult result = multimodalAnalysisService.transcribeAudio(testAudioPath);

            assertNotNull(result, "转录结果不能为空");
            assertNotNull(result.getText(), "转录文本不能为空");
            log.info("语音转文字成功: text={}, confidence={}",
                    result.getText(), result.getConfidence());

        } catch (Exception e) {
            log.error("语音转文字测试失败: {}", e.getMessage(), e);
            fail("语音转文字失败: " + e.getMessage());
        }
    }

    @Test
    public void testImageUnderstanding() {
        log.info("测试图像理解");

        String testImagePath = getTestImagePath();
        if (testImagePath == null) {
            log.warn("测试图像不存在，跳过测试");
            return;
        }

        try {
            String question = "这张图片展示了什么内容？";
            String result = multimodalAnalysisService.understandImage(testImagePath, question);

            assertNotNull(result, "理解结果不能为空");
            log.info("图像理解成功: {}", result);

        } catch (Exception e) {
            log.error("图像理解测试失败: {}", e.getMessage(), e);
            fail("图像理解失败: " + e.getMessage());
        }
    }

    @Test
    public void testVideoAnalysis() {
        log.info("测试视频分析");

        VideoAnalysisRequest request = VideoAnalysisRequest.builder()
                .videoId(1L)
                .transcript("这是一段测试转录文本，描述了智能驾驶系统的表现。")
                .vehicleName("测试车辆A")
                .domainName("智能驾驶")
                .indexName("车道保持")
                .videoType("good")
                .taskType("车道保持测试")
                .description("测试车辆在高速公路上的车道保持能力")
                .build();

        try {
            VideoAnalysisResult result = multimodalAnalysisService.analyzeVideo(request);

            assertNotNull(result, "分析结果不能为空");
            assertNotNull(result.getOverallScore(), "总分不能为空");
            assertNotNull(result.getSummary(), "总结不能为空");

            log.info("视频分析成功: overallScore={}, summary={}",
                    result.getOverallScore(), result.getSummary());
            log.info("处理时间: {}ms", result.getProcessingTimeMs());

            if (result.getDimensions() != null) {
                log.info("各维度评分:");
                result.getDimensions().forEach((key, value) -> {
                    log.info("  {}: {} - {}", key, value.getScore(), value.getComment());
                });
            }

        } catch (Exception e) {
            log.error("视频分析测试失败: {}", e.getMessage(), e);
            fail("视频分析失败: " + e.getMessage());
        }
    }

    @Test
    public void testVideoAnalysisAsync() {
        log.info("测试异步视频分析");

        Long testVideoId = getTestVideoId();
        if (testVideoId == null) {
            log.warn("测试视频ID不存在，跳过测试");
            return;
        }

        try {
            CompletableFuture<VideoAnalysisResult> future = videoAnalysisService.analyzeVideoAsync(testVideoId);

            VideoAnalysisResult result = future.get(60, TimeUnit.SECONDS);

            assertNotNull(result, "分析结果不能为空");
            log.info("异步视频分析成功: videoId={}, score={}", testVideoId, result.getOverallScore());

        } catch (Exception e) {
            log.error("异步视频分析测试失败: {}", e.getMessage(), e);
            fail("异步视频分析失败: " + e.getMessage());
        }
    }

    @Test
    public void testVideoAnalysisStatus() {
        log.info("测试视频分析状态检查");

        Long testVideoId = getTestVideoId();
        if (testVideoId == null) {
            log.warn("测试视频ID不存在，跳过测试");
            return;
        }

        boolean analyzed = videoAnalysisService.isVideoAnalyzed(testVideoId);
        log.info("视频是否已分析: videoId={}, analyzed={}", testVideoId, analyzed);

        if (analyzed) {
            VideoAnalysisResult result = videoAnalysisService.getAnalysisResult(testVideoId);
            assertNotNull(result, "分析结果不能为空");
            log.info("获取分析结果成功: score={}", result.getOverallScore());
        }
    }

    @Test
    public void testQaWithVideoAnalysis() {
        log.info("测试智能问答集成视频分析");

        try {
            QaSessionRequest sessionRequest = new QaSessionRequest();
            sessionRequest.setUserId("video_test_user");
            sessionRequest.setUserName("视频测试用户");

            QaSessionResponse sessionResponse = qaSessionService.createSession(sessionRequest);
            assertNotNull(sessionResponse.getSessionId(), "会话ID不能为空");

            QaQueryRequest queryRequest = new QaQueryRequest();
            queryRequest.setSessionId(sessionResponse.getSessionId());
            queryRequest.setQuestion("查询车辆在智能驾驶功能域的视频表现如何？");
            queryRequest.setUserId("video_test_user");

            QaQueryResponse response = qaQueryService.processQuery(queryRequest);

            assertNotNull(response, "查询响应不能为空");
            log.info("查询处理成功: queryId={}", response.getQueryId());
            log.info("答案: {}", response.getAnswer() != null ? response.getAnswer().getText() : "无答案");

        } catch (Exception e) {
            log.error("智能问答集成测试失败: {}", e.getMessage(), e);
            fail("智能问答集成失败: " + e.getMessage());
        }
    }

    @Test
    public void testBatchVideoAnalysis() {
        log.info("测试批量视频分析");

        List<Long> videoIds = getTestVideoIds();
        if (videoIds == null || videoIds.isEmpty()) {
            log.warn("测试视频ID列表不存在，跳过测试");
            return;
        }

        try {
            List<String> taskIds = videoAnalysisService.analyzeVideosBatch(videoIds);

            assertNotNull(taskIds, "任务ID列表不能为空");
            assertEquals(videoIds.size(), taskIds.size(), "任务数量应该匹配");
            log.info("批量视频分析启动成功，任务数: {}", taskIds.size());

        } catch (Exception e) {
            log.error("批量视频分析测试失败: {}", e.getMessage(), e);
            fail("批量视频分析失败: " + e.getMessage());
        }
    }

    private String getTestVideoPath() {
        String videoPath = System.getProperty("test.video.path");
        if (videoPath != null && !videoPath.isEmpty()) {
            return videoPath;
        }
        return null;
    }

    private String getTestAudioPath() {
        String audioPath = System.getProperty("test.audio.path");
        if (audioPath != null && !audioPath.isEmpty()) {
            return audioPath;
        }
        return null;
    }

    private String getTestImagePath() {
        String imagePath = System.getProperty("test.image.path");
        if (imagePath != null && !imagePath.isEmpty()) {
            return imagePath;
        }
        return null;
    }

    private Long getTestVideoId() {
        String videoIdStr = System.getProperty("test.video.id");
        if (videoIdStr != null && !videoIdStr.isEmpty()) {
            try {
                return Long.parseLong(videoIdStr);
            } catch (NumberFormatException e) {
                log.warn("无法解析视频ID: {}", videoIdStr);
            }
        }

        List<FunctionDomainVideoEntity> videos = functionDomainVideoService.list();
        if (videos != null && !videos.isEmpty()) {
            return videos.get(0).getId();
        }

        return null;
    }

    private List<Long> getTestVideoIds() {
        List<FunctionDomainVideoEntity> videos = functionDomainVideoService.list();
        if (videos != null && !videos.isEmpty()) {
            return videos.stream()
                    .limit(3)
                    .map(FunctionDomainVideoEntity::getId)
                    .toList();
        }
        return null;
    }
}
