package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.xailab.vehicle.xaivehicledata.config.QaLlmConfig;
import com.xailab.vehicle.xaivehicledata.service.QaLlmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * 通义千问API服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaLlmServiceImpl implements QaLlmService {

    private final QaLlmConfig llmConfig;

    // 调用统计
    private volatile long totalCalls = 0;
    private volatile long successfulCalls = 0;
    private volatile long failedCalls = 0;
    private volatile double averageResponseTime = 0;
    private volatile long lastCallTime = 0;

    @PostConstruct
    public void init() {
        // 验证API密钥配置
        if (llmConfig.getApiKey() != null && !llmConfig.getApiKey().isEmpty()) {
            log.info("通义千问API已初始化，API Key: {}", llmConfig.getApiKey().substring(0, 8) + "...");
        } else {
            log.warn("通义千问API Key未配置，请在配置文件中设置 qa.llm.api-key");
        }
    }

    @Override
    public String callLLM(String prompt, String model, Double temperature) {
        long startTime = System.currentTimeMillis();

        try {
            totalCalls++;

            if (model == null || model.isEmpty()) {
                model = llmConfig.getDefaultModel();
            }

            if (temperature == null) {
                temperature = getModelTemperature(model);
            }

            // 构建消息
            Message message = Message.builder()
                    .role(Role.USER.getValue())
                    .content(prompt)
                    .build();

            // 构建生成参数
            GenerationParam param = GenerationParam.builder()
                    .apiKey(llmConfig.getApiKey())
                    .model(model)
                    .messages(List.of(message))
                    .temperature(temperature.floatValue())
                    .maxTokens(llmConfig.getMaxOutputLength())
                    .build();

            // 调用API
            Generation gen = new Generation();
            GenerationResult result = gen.call(param);

            log.debug("通义千问API返回结果: result={}, output={}, choices={}", 
                    result != null, 
                    result != null && result.getOutput() != null,
                    result != null && result.getOutput() != null && result.getOutput().getChoices() != null);

            if (result != null && result.getOutput() != null 
                    && result.getOutput().getChoices() != null 
                    && !result.getOutput().getChoices().isEmpty()) {
                String response = result.getOutput().getChoices().get(0).getMessage().getContent();
                successfulCalls++;
                updateAverageResponseTime(System.currentTimeMillis() - startTime);
                lastCallTime = System.currentTimeMillis();

                log.debug("通义千问API调用成功，耗时: {}ms, 响应长度: {}", 
                        System.currentTimeMillis() - startTime, response.length());
                return response;
            } else {
                failedCalls++;
                log.warn("通义千问API返回空结果: result={}, output={}, choices={}", 
                        result != null, 
                        result != null && result.getOutput() != null,
                        result != null && result.getOutput() != null && result.getOutput().getChoices() != null);
                
                if (result != null) {
                    log.warn("API返回的完整信息: request_id={}, status_code={}, message={}", 
                            result.getRequestId(), 
                            result.getUsage(),
                            result.getOutput());
                }
                return "";
            }

        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            failedCalls++;
            log.error("通义千问API调用异常: {}", e.getMessage(), e);

            if (shouldRetry(e)) {
                return retryCall(prompt, model, temperature);
            }

            throw new RuntimeException("大语言模型调用失败: " + e.getMessage(), e);
        } catch (Exception e) {
            failedCalls++;
            log.error("通义千问API未知异常: {}", e.getMessage(), e);
            throw new RuntimeException("大语言模型调用失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String understandQuestion(String question) {
        String prompt = buildQuestionUnderstandingPrompt(question);
        return callLLM(prompt, getModelForComplexity("simple"), 0.3);
    }

    @Override
    public String generateSql(String prompt) {
        return callLLM(prompt, getModelForComplexity("medium"), 0.1);
    }

    @Override
    public String generateAnswer(String prompt) {
        return callLLM(prompt, getModelForComplexity("medium"), 0.7);
    }

    @Override
    public String handleError(String prompt) {
        return callLLM(prompt, getModelForComplexity("simple"), 0.5);
    }

    @Override
    public String recommendVisualization(String prompt) {
        return callLLM(prompt, getModelForComplexity("simple"), 0.3);
    }

    @Override
    public String matchTemplate(String prompt) {
        return callLLM(prompt, getModelForComplexity("simple"), 0.2);
    }

    @Override
    public String validateData(String prompt) {
        return callLLM(prompt, getModelForComplexity("simple"), 0.1);
    }

    @Override
    public boolean isHealthy() {
        try {
            // 发送一个简单的健康检查请求
            String testPrompt = "请回答：1+1等于几？";
            callLLM(testPrompt, llmConfig.getDefaultModel(), 0.1);
            return true;
        } catch (Exception e) {
            log.warn("通义千问API健康检查失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String[] getSupportedModels() {
        return new String[] {
                "qwen-turbo",
                "qwen-plus",
                "qwen-max",
                "qwen-max-longcontext"
        };
    }

    @Override
    public LlmStats getStats() {
        return new LlmStats(totalCalls, successfulCalls, failedCalls,
                averageResponseTime, lastCallTime);
    }

    /**
     * 重试调用
     */
    private String retryCall(String prompt, String model, Double temperature) {
        for (int i = 0; i < llmConfig.getMaxRetries(); i++) {
            try {
                log.info("第{}次重试通义千问API调用", i + 1);
                Thread.sleep(llmConfig.getRetryIntervalMs());
                return callLLM(prompt, model, temperature);
            } catch (Exception e) {
                log.warn("第{}次重试失败: {}", i + 1, e.getMessage());
                if (i == llmConfig.getMaxRetries() - 1) {
                    throw new RuntimeException("重试后仍然失败: " + e.getMessage(), e);
                }
            }
        }
        return "";
    }

    /**
     * 判断是否应该重试
     */
    private boolean shouldRetry(Exception e) {
        // 网络错误、超时等可以重试
        String message = e.getMessage().toLowerCase();
        return message.contains("timeout") ||
                message.contains("connection") ||
                message.contains("network") ||
                message.contains("rate limit");
    }

    /**
     * 根据复杂度选择模型
     */
    private String getModelForComplexity(String complexity) {
        switch (complexity.toLowerCase()) {
            case "simple":
                return "qwen-turbo";
            case "medium":
                return "qwen-plus";
            case "complex":
            case "high":
                return "qwen-max";
            default:
                return llmConfig.getDefaultModel();
        }
    }

    /**
     * 获取模型默认温度
     */
    private Double getModelTemperature(String model) {
        if ("qwen-turbo".equals(model)) {
            return llmConfig.getModels().getTurbo().getTemperature();
        } else if ("qwen-plus".equals(model)) {
            return llmConfig.getModels().getPlus().getTemperature();
        } else if ("qwen-max".equals(model)) {
            return llmConfig.getModels().getMax().getTemperature();
        } else if ("qwen-max-longcontext".equals(model)) {
            return llmConfig.getModels().getLongContext().getTemperature();
        }
        return llmConfig.getDefaultTemperature();
    }

    /**
     * 更新平均响应时间
     */
    private synchronized void updateAverageResponseTime(long responseTime) {
        if (successfulCalls == 1) {
            averageResponseTime = responseTime;
        } else {
            averageResponseTime = (averageResponseTime * (successfulCalls - 1) + responseTime) / successfulCalls;
        }
    }

    /**
     * 构建问题理解Prompt
     */
    private String buildQuestionUnderstandingPrompt(String question) {
        return String.format("""
                你是一个专业的汽车智能驾驶评估系统助手。请分析用户的问题，提取以下信息：

                问题：%s

                请返回JSON格式：
                {
                  "intent": "query_data|compare_data|analyze_trend|ask_explanation|get_statistics",
                  "entities": ["entity1", "entity2"],
                  "query_type": "single_table|multi_table|aggregation|comparison",
                  "visualization_preference": "table|chart|mixed",
                  "complexity": "simple|medium|complex",
                  "time_range": "recent|month|quarter|year|all",
                  "data_scope": "single_vehicle|multiple_vehicles|all_vehicles|specific_domain"
                }
                """, question);
    }
}
