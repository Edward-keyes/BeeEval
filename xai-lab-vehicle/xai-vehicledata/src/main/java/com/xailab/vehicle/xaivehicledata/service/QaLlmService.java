package com.xailab.vehicle.xaivehicledata.service;

/**
 * 大语言模型服务接口
 */
public interface QaLlmService {

    /**
     * 调用大语言模型
     */
    String callLLM(String prompt, String model, Double temperature);

    /**
     * 问题理解
     */
    String understandQuestion(String question);

    /**
     * SQL生成
     */
    String generateSql(String prompt);

    /**
     * 答案生成
     */
    String generateAnswer(String prompt);

    /**
     * 错误处理
     */
    String handleError(String prompt);

    /**
     * 可视化推荐
     */
    String recommendVisualization(String prompt);

    /**
     * 模板匹配
     */
    String matchTemplate(String prompt);

    /**
     * 数据验证
     */
    String validateData(String prompt);

    /**
     * 检查服务健康状态
     */
    boolean isHealthy();

    /**
     * 获取支持的模型列表
     */
    String[] getSupportedModels();

    /**
     * 获取调用统计
     */
    LlmStats getStats();

    /**
     * 统计类
     */
    class LlmStats {
        private long totalCalls;
        private long successfulCalls;
        private long failedCalls;
        private double averageResponseTime;
        private long lastCallTime;

        public LlmStats(long totalCalls, long successfulCalls, long failedCalls,
                double averageResponseTime, long lastCallTime) {
            this.totalCalls = totalCalls;
            this.successfulCalls = successfulCalls;
            this.failedCalls = failedCalls;
            this.averageResponseTime = averageResponseTime;
            this.lastCallTime = lastCallTime;
        }

        // Getters
        public long getTotalCalls() {
            return totalCalls;
        }

        public long getSuccessfulCalls() {
            return successfulCalls;
        }

        public long getFailedCalls() {
            return failedCalls;
        }

        public double getAverageResponseTime() {
            return averageResponseTime;
        }

        public long getLastCallTime() {
            return lastCallTime;
        }
    }
}
