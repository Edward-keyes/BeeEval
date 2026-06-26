package com.xailab.vehicle.xaivehicledata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 通义千问配置
 */
@Configuration
@ConfigurationProperties(prefix = "qa.llm")
@Data
public class QaLlmConfig {

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 默认模型
     */
    private String defaultModel = "qwen-turbo";

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    /**
     * 重试间隔(毫秒)
     */
    private long retryIntervalMs = 1000;

    /**
     * 请求超时时间(秒)
     */
    private int requestTimeoutSeconds = 30;

    /**
     * 最大输入长度
     */
    private int maxInputLength = 8000;

    /**
     * 最大输出长度
     */
    private int maxOutputLength = 2000;

    /**
     * 默认温度参数
     */
    private double defaultTemperature = 0.7;

    /**
     * 模型配置
     */
    private ModelConfig models = new ModelConfig();

    @Data
    public static class ModelConfig {
        private ModelSettings turbo = new ModelSettings("qwen-turbo", 0.7, 8000, 1500);
        private ModelSettings plus = new ModelSettings("qwen-plus", 0.7, 30000, 4000);
        private ModelSettings max = new ModelSettings("qwen-max", 0.8, 6000, 6000);
        private ModelSettings longContext = new ModelSettings("qwen-max-longcontext", 0.7, 28000, 2000);
    }

    @Data
    public static class ModelSettings {
        private String name;
        private double temperature;
        private int maxInputTokens;
        private int maxOutputTokens;

        public ModelSettings() {
        }

        public ModelSettings(String name, double temperature, int maxInputTokens, int maxOutputTokens) {
            this.name = name;
            this.temperature = temperature;
            this.maxInputTokens = maxInputTokens;
            this.maxOutputTokens = maxOutputTokens;
        }
    }
}
