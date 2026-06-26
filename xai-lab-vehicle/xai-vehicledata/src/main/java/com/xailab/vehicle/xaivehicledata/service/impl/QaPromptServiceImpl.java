package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaivehicledata.config.QaPromptConfig;
import com.xailab.vehicle.xaivehicledata.service.QaPromptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Prompt模板服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaPromptServiceImpl implements QaPromptService {

    private final QaPromptConfig promptConfig;

    @Override
    public String buildQuestionUnderstandingPrompt(String question) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        return replaceVariables(promptConfig.getQuestionUnderstanding(), variables);
    }

    @Override
    public String buildSqlGenerationPrompt(String question, String tableSchemas,
            String context, String historyQueries) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        variables.put("table_schemas", tableSchemas != null ? tableSchemas : getDefaultTableSchemas());
        variables.put("context", context != null ? context : "");
        variables.put("history_queries", historyQueries != null ? historyQueries : "");
        return replaceVariables(promptConfig.getSqlGeneration(), variables);
    }

    @Override
    public String buildAnswerGenerationPrompt(String question, Object queryResult,
            String fieldDescriptions, String visualizationSuggestion,
            String context) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        variables.put("query_result", queryResult != null ? formatQueryResult(queryResult) : "[]");
        variables.put("field_descriptions", fieldDescriptions != null ? fieldDescriptions : "[]");
        variables.put("visualization_suggestion", visualizationSuggestion != null ? visualizationSuggestion : "{}");
        variables.put("context", context != null ? context : "{}");
        return replaceVariables(promptConfig.getAnswerGeneration(), variables);
    }

    @Override
    public String buildErrorHandlingPrompt(String errorType, String errorMessage,
            String originalQuestion, String executedSql) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("error_type", errorType);
        variables.put("error_message", errorMessage);
        variables.put("original_question", originalQuestion);
        variables.put("executed_sql", executedSql != null ? executedSql : "");
        return replaceVariables(promptConfig.getErrorHandling(), variables);
    }

    @Override
    public String buildVisualizationPrompt(String dataCharacteristics, String queryIntent,
            String dataSample, String userPreference) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("data_characteristics", dataCharacteristics != null ? dataCharacteristics : "{}");
        variables.put("query_intent", queryIntent != null ? queryIntent : "query_data");
        variables.put("data_sample", dataSample != null ? dataSample : "[]");
        variables.put("user_preference", userPreference != null ? userPreference : "table");
        return replaceVariables(promptConfig.getVisualizationRecommendation(), variables);
    }

    @Override
    public String buildConversationPrompt(String currentQuestion, String conversationHistory,
            String currentTopic, String activeEntities, String userPreferences) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("current_question", currentQuestion);
        variables.put("conversation_history", conversationHistory != null ? conversationHistory : "[]");
        variables.put("current_topic", currentTopic != null ? currentTopic : "general");
        variables.put("active_entities", activeEntities != null ? activeEntities : "{}");
        variables.put("user_preferences", userPreferences != null ? userPreferences : "{}");
        return replaceVariables(promptConfig.getConversationContext(), variables);
    }

    @Override
    public String buildTemplateMatchingPrompt(String question, String availableTemplates,
            String matchingHistory) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        variables.put("available_templates", availableTemplates != null ? availableTemplates : "[]");
        variables.put("matching_history", matchingHistory != null ? matchingHistory : "[]");
        return replaceVariables(promptConfig.getTemplateMatching(), variables);
    }

    @Override
    public String buildDataValidationPrompt(String sql, String result, String fields, String businessRules) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sql", sql);
        variables.put("result", result != null ? result : "[]");
        variables.put("fields", fields != null ? fields : "[]");
        variables.put("business_rules", businessRules != null ? businessRules : "[]");
        return replaceVariables(promptConfig.getDataValidation(), variables);
    }

    @Override
    public String getSystemRole() {
        return promptConfig.getSystemRole();
    }

    @Override
    public String replaceVariables(String template, Map<String, Object> variables) {
        String result = template;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }

    /**
     * 获取默认表结构描述
     * 车辆维度：无 vehicle_info 表；车辆 = vehicle_brand（品牌）+
     * vehicle_base_info（型号），vehicle_id 即 vehicle_base_info.id。
     * 建议将车辆列表（id 与 品牌+型号 对应）同步到 qa_knowledge_base 或作为上下文注入，便于模型从自然语言解析出正确的
     * vehicleIds。
     */
    private String getDefaultTableSchemas() {
        return """
                [
                  {
                    "table_name": "vehicle_brand",
                    "description": "车辆品牌表。品牌名如小米、蔚来",
                    "columns": [
                      {"name": "id", "type": "bigint", "description": "主键ID"},
                      {"name": "brand", "type": "varchar", "description": "品牌名称，如小米、蔚来"},
                      {"name": "brand_en", "type": "varchar", "description": "品牌英文名称"}
                    ]
                  },
                  {
                    "table_name": "vehicle_base_info",
                    "description": "车辆基础信息表。车型=品牌+型号，如小米SU7中SU7为型号",
                    "columns": [
                      {"name": "id", "type": "bigint", "description": "主键ID，即各表中 vehicle_id 所指"},
                      {"name": "brand_id", "type": "bigint", "description": "关联 vehicle_brand.id"},
                      {"name": "vehicle_model", "type": "varchar", "description": "车型/型号，如SU7"},
                      {"name": "vehicle_version", "type": "varchar", "description": "汽车版本"},
                      {"name": "energy_type", "type": "varchar", "description": "能源类型"}
                    ],
                    "note": "车辆展示名 = vehicle_brand.brand + vehicle_base_info.vehicle_model"
                  },
                  {
                    "table_name": "vehicle_functional_domain",
                    "description": "功能域表，存储车辆功能域信息",
                    "columns": [
                      {"name": "id", "type": "bigint", "description": "主键ID"},
                      {"name": "functional_domain_name", "type": "varchar", "description": "功能域名称"},
                      {"name": "status", "type": "int", "description": "状态"}
                    ]
                  },
                  {
                    "table_name": "vehicle_domain_index",
                    "description": "功能域三级指标表，存储功能域下的具体指标",
                    "columns": [
                      {"name": "id", "type": "bigint", "description": "主键ID"},
                      {"name": "functional_domain", "type": "bigint", "description": "关联 vehicle_functional_domain.id，表示该指标所属的功能域"},
                      {"name": "index_name", "type": "varchar", "description": "指标名称"},
                      {"name": "definition", "type": "varchar", "description": "指标定义"},
                      {"name": "status", "type": "int", "description": "状态"}
                    ],
                    "note": "通过 functional_domain 字段关联 vehicle_functional_domain.id 可查询该指标所属的功能域名称"
                  },
                  {
                    "table_name": "vehicle_domain_score",
                    "description": "车辆功能域/指标评分表。vehicle_id 关联 vehicle_base_info.id",
                    "columns": [
                      {"name": "id", "type": "bigint", "description": "主键ID"},
                      {"name": "vehicle_id", "type": "bigint", "description": "车辆ID，即 vehicle_base_info.id"},
                      {"name": "domain_id", "type": "bigint", "description": "功能域/指标ID，根据type字段关联不同表"},
                      {"name": "type", "type": "tinyint", "description": "评分类型：1-功能域总分（关联vehicle_functional_domain.id），2-三级指标分（关联vehicle_domain_index.id），3-基础能力分（关联vehicle_domain_index.id）"},
                      {"name": "score", "type": "double", "description": "评分值"}
                    ],
                    "note": "type=1时，domain_id关联vehicle_functional_domain.id；type=2或3时，domain_id关联vehicle_domain_index.id",
                    "indexes": ["vehicle_id", "domain_id", "type"]
                  },
                  {
                    "table_name": "beeeval_open_case_score",
                    "description": "开源题库评分表。vehicle_id 关联 vehicle_base_info.id",
                    "columns": [
                      {"name": "id", "type": "bigint", "description": "主键ID"},
                      {"name": "vehicle_id", "type": "bigint", "description": "车辆ID，即 vehicle_base_info.id"},
                      {"name": "three_tag_id", "type": "bigint", "description": "三级指标ID，关联vehicle_domain_index.id"},
                      {"name": "case_id", "type": "int", "description": "用例ID"},
                      {"name": "score", "type": "int", "description": "评分值"}
                    ],
                    "indexes": ["vehicle_id", "case_id"]
                  },
                  {
                    "table_name": "beeeval_open_source_case",
                    "description": "开源用例表，存储开源题库的用例信息",
                    "columns": [
                      {"name": "id", "type": "bigint", "description": "主键ID"},
                      {"name": "three_tag_id", "type": "bigint", "description": "三级指标ID，关联vehicle_domain_index.id"},
                      {"name": "case_name", "type": "varchar", "description": "用例名称"},
                      {"name": "case_name_en", "type": "varchar", "description": "用例英文名称"},
                      {"name": "status", "type": "int", "description": "状态"}
                    ]
                  },
                  {
                    "table_name": "vehicle_function_domain_video",
                    "description": "车辆功能域视频表，存储车辆在功能域三级指标下的视频数据",
                    "columns": [
                      {"name": "id", "type": "bigint", "description": "主键ID"},
                      {"name": "vehicle_id", "type": "bigint", "description": "车辆ID，即 vehicle_base_info.id"},
                      {"name": "function_domain_id", "type": "bigint", "description": "功能域ID，关联vehicle_functional_domain.id"},
                      {"name": "function_domain_index_id", "type": "bigint", "description": "功能域三级指标ID，关联vehicle_domain_index.id"},
                      {"name": "type", "type": "int", "description": "视频类型：0=bad（表现不好），1=good（表现好）"},
                      {"name": "url_name", "type": "varchar", "description": "视频名称"},
                      {"name": "file_type", "type": "int", "description": "文件类型：1=mp4，2=srt"},
                      {"name": "is_srt", "type": "int", "description": "是否有字幕：1=有，0或null=没有"},
                      {"name": "title", "type": "varchar", "description": "标题中文"},
                      {"name": "title_en", "type": "varchar", "description": "标题英文"},
                      {"name": "task_type", "type": "varchar", "description": "任务类型中文"},
                      {"name": "task_type_en", "type": "varchar", "description": "任务类型英文"},
                      {"name": "description", "type": "varchar", "description": "详情中文"},
                      {"name": "description_en", "type": "varchar", "description": "详情英文"},
                      {"name": "file_name", "type": "varchar", "description": "视频文件名"},
                      {"name": "status", "type": "int", "description": "状态：0=不可用，1=可用"}
                    ]
                  }
                ]
                """;
    }

    /**
     * 格式化查询结果用于Prompt
     */
    private String formatQueryResult(Object queryResult) {
        if (queryResult == null) {
            return "[]";
        }

        try {
            // 如果是复杂对象，转换为JSON字符串
            if (queryResult instanceof String) {
                return (String) queryResult;
            } else {
                // 这里应该使用JSON序列化工具
                return queryResult.toString();
            }
        } catch (Exception e) {
            log.warn("Failed to format query result: {}", e.getMessage());
            return "[]";
        }
    }
}
