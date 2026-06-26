package com.xailab.vehicle.xaivehicledata.service;

import java.util.Map;

/**
 * Prompt模板服务接口
 */
public interface QaPromptService {

    /**
     * 构建问题理解Prompt
     */
    String buildQuestionUnderstandingPrompt(String question);

    /**
     * 构建SQL生成Prompt
     */
    String buildSqlGenerationPrompt(String question, String tableSchemas,
                                   String context, String historyQueries);

    /**
     * 构建答案生成Prompt
     */
    String buildAnswerGenerationPrompt(String question, Object queryResult,
                                      String fieldDescriptions, String visualizationSuggestion,
                                      String context);

    /**
     * 构建错误处理Prompt
     */
    String buildErrorHandlingPrompt(String errorType, String errorMessage,
                                   String originalQuestion, String executedSql);

    /**
     * 构建可视化推荐Prompt
     */
    String buildVisualizationPrompt(String dataCharacteristics, String queryIntent,
                                   String dataSample, String userPreference);

    /**
     * 构建对话上下文Prompt
     */
    String buildConversationPrompt(String currentQuestion, String conversationHistory,
                                 String currentTopic, String activeEntities, String userPreferences);

    /**
     * 构建模板匹配Prompt
     */
    String buildTemplateMatchingPrompt(String question, String availableTemplates,
                                     String matchingHistory);

    /**
     * 构建数据验证Prompt
     */
    String buildDataValidationPrompt(String sql, String result, String fields, String businessRules);

    /**
     * 获取系统角色定义
     */
    String getSystemRole();

    /**
     * 替换Prompt模板变量
     */
    String replaceVariables(String template, Map<String, Object> variables);
}
