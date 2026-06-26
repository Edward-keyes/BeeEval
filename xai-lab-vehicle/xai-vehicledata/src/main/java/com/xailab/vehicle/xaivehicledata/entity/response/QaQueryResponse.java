package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 问答查询响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QaQueryResponse {

    /**
     * 查询ID
     */
    private String queryId;

    /**
     * 响应类型
     */
    private String responseType;

    /**
     * 置信度分数 (0.0-1.0)
     */
    private Double confidenceScore;

    /**
     * 答案信息
     */
    private AnswerInfo answer;

    /**
     * 查询信息
     */
    private QueryInfo queryInfo;

    /**
     * 可视化信息
     */
    private VisualizationInfo visualization;

    /**
     * 相关问题建议
     */
    private List<String> followUpQuestions;

    /**
     * 处理耗时(毫秒)
     */
    private Long processingTime;

    /**
     * 错误信息（可选）
     */
    private ErrorInfo error;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerInfo {
        private String text;
        private List<String> keyPoints;
        private String dataSummary;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryInfo {
        private String sqlGenerated;
        private String dataSource;
        private Integer recordCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisualizationInfo {
        private String recommendedType;
        private Object dataStructure;
        private Map<String, Object> configSuggestion;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorInfo {
        private String errorType;
        private String errorCode;
        private String errorMessage;
        private String suggestion;
    }
}
