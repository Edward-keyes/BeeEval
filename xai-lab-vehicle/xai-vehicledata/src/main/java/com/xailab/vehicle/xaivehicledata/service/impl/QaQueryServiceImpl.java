package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.xaivehicledata.dao.QaQueryHistoryDao;
import com.xailab.vehicle.xaivehicledata.dto.VideoVectorSearchResult;
import com.xailab.vehicle.xaivehicledata.entity.QaQueryHistoryEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.QaQueryRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.QaQueryResponse;
import com.xailab.vehicle.xaivehicledata.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.Map;

/**
 * 问答查询服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaQueryServiceImpl implements QaQueryService {

    private final QaQueryHistoryDao qaQueryHistoryDao;
    private final QaCacheService qaCacheService;
    private final QaAuditService qaAuditService;
    private final QaTemplateService qaTemplateService;
    private final QaVisualizationService qaVisualizationService;
    private final QaKnowledgeBaseService qaKnowledgeBaseService;
    private final QaSqlExecutorService qaSqlExecutorService;
    private final QaLlmService qaLlmService;
    private final QaPromptService qaPromptService;
    private final VideoAnalysisOrchestrationService videoAnalysisService;
    private final VideoVectorSearchService videoVectorSearchService;

    @Autowired
    private QaSessionService qaSessionService;

    // SQL注入关键词黑名单
    private static final List<String> SQL_BLACKLIST = Arrays.asList(
            "drop", "delete", "update", "insert", "alter", "create", "truncate",
            "exec", "execute", "union", "select.*into", "load_file", "outfile");

    @Override
    @Transactional
    public QaQueryResponse processQuery(QaQueryRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            // 1. 更新会话活动时间
            qaSessionService.updateSessionActivity(request.getSessionId());

            // 2. 生成缓存键
            String cacheKey = qaCacheService.generateCacheKey(
                    request.getQuestion(),
                    request.getUserId(),
                    request.getParameters());

            // 3. 尝试从缓存获取结果
            QaQueryResponse cachedResponse = qaCacheService.getCache(cacheKey);
            if (cachedResponse != null) {
                log.info("Cache hit for question: {}", request.getQuestion());
                return cachedResponse;
            }

            // 4. 检索相关车辆信息（作为上下文）
            String vehicleContext = retrieveVehicleContext(request.getQuestion());

            // 5. 问题理解与意图识别
            QueryContext context = analyzeQueryIntent(request.getQuestion());

            // 6. 模板匹配（如果未指定模板）
            if (request.getTemplateId() == null) {
                var matchedTemplate = qaTemplateService.matchTemplate(request.getQuestion());
                if (matchedTemplate != null) {
                    context.setTemplateId(matchedTemplate.getId());
                }
            }

            // 7. SQL生成（传入车辆上下文）
            String generatedSql = generateSql(request.getQuestion(), context, vehicleContext);
            if (!validateSql(generatedSql)) {
                throw new IllegalArgumentException("生成的SQL包含不安全操作");
            }

            // 8. 执行SQL查询
            Object queryResult = executeSql(generatedSql);
            int recordCount = getRecordCount(queryResult);

            // 8. 生成自然语言答案
            String answer = generateAnswer(request.getQuestion(), queryResult, context);

            // 9. 可视化推荐
            String vizType = qaVisualizationService.recommendChartType(queryResult, context.getIntent());
            Object vizData = qaVisualizationService.transformDataForVisualization(queryResult, vizType);

            // 10. 保存查询历史
            QaQueryHistoryEntity history = saveQueryHistory(request, generatedSql, queryResult,
                    answer, vizType, startTime);

            // 11. 审计日志记录
            qaAuditService.logQueryAudit(
                    request.getUserId(), request.getUserName(), request.getSessionId(),
                    history.getId(), request.getQuestion(), generatedSql, recordCount,
                    getClientIp(), getUserAgent(), System.currentTimeMillis() - startTime,
                    isSensitiveQuery(request.getQuestion()), "low");

            // 12. 构建响应
            QaQueryResponse response = buildResponse(history.getId().toString(), context,
                    answer, generatedSql, queryResult,
                    vizType, vizData, startTime);

            // 13. 缓存结果
            qaCacheService.setCache(cacheKey, response, 1800); // 30分钟缓存

            return response;

        } catch (Exception e) {
            log.error("Error processing query: {}", request.getQuestion(), e);

            // 保存失败的查询历史
            saveFailedQueryHistory(request, e.getMessage(), startTime);

            // 返回错误响应
            return QaQueryResponse.builder()
                    .responseType("error")
                    .error(QaQueryResponse.ErrorInfo.builder()
                            .errorType("processing_error")
                            .errorCode("ERR_001")
                            .errorMessage("查询处理失败: " + e.getMessage())
                            .suggestion("请检查问题描述是否清晰，或联系技术支持")
                            .build())
                    .processingTime(System.currentTimeMillis() - startTime)
                    .build();
        }
    }

    @Override
    public QaQueryResponse regenerateAnswer(String queryId) {
        // 简化实现：重新执行相同查询
        QaQueryHistoryEntity history = qaQueryHistoryDao.selectById(queryId);
        if (history == null) {
            throw new IllegalArgumentException("查询记录不存在");
        }

        // 重新生成答案的逻辑
        Object queryResult = executeSql(history.getGeneratedSql());
        String newAnswer = generateAnswer(history.getQuestion(), queryResult, null);

        // 更新历史记录
        history.setAnswer(newAnswer);
        qaQueryHistoryDao.updateById(history);

        return buildResponse(queryId, null, newAnswer, history.getGeneratedSql(),
                queryResult, history.getVisualizationType(), null, 0L);
    }

    @Override
    public QaQueryResponse getQueryDetail(String queryId) {
        QaQueryHistoryEntity history = qaQueryHistoryDao.selectById(queryId);
        if (history == null) {
            return null;
        }

        return QaQueryResponse.builder()
                .queryId(queryId)
                .answer(QaQueryResponse.AnswerInfo.builder()
                        .text(history.getAnswer())
                        .build())
                .queryInfo(QaQueryResponse.QueryInfo.builder()
                        .sqlGenerated(history.getGeneratedSql())
                        .recordCount(getRecordCount(JSON.parse(history.getSqlResult())))
                        .build())
                .visualization(QaQueryResponse.VisualizationInfo.builder()
                        .recommendedType(history.getVisualizationType())
                        .build())
                .build();
    }

    @Override
    public boolean validateSql(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return false;
        }

        String lowerSql = sql.toLowerCase();
        return SQL_BLACKLIST.stream()
                .noneMatch(keyword -> Pattern.compile("\\b" + keyword + "\\b").matcher(lowerSql).find());
    }

    @Override
    public Object executeSql(String sql) {
        log.info("执行SQL: {}", sql);

        try {
            List<Map<String, Object>> result = qaSqlExecutorService.executeQuery(sql);
            log.info("SQL执行成功，返回{}条记录", result.size());
            return result;
        } catch (Exception e) {
            log.error("SQL执行失败: sql={}, error={}", sql, e.getMessage(), e);
            throw new RuntimeException("SQL执行失败: " + e.getMessage(), e);
        }
    }

    private QueryContext analyzeQueryIntent(String question) {
        log.info("分析问题意图: question={}", question);

        try {
            String prompt = qaPromptService.buildQuestionUnderstandingPrompt(question);
            log.debug("问题理解Prompt: {}", prompt);

            String llmResponse = qaLlmService.understandQuestion(prompt);
            log.info("LLM返回的问题分析: {}", llmResponse);

            if (llmResponse != null && !llmResponse.trim().isEmpty()) {
                return parseQueryContextFromLlm(llmResponse);
            }
        } catch (Exception e) {
            log.error("调用LLM分析问题意图失败，使用默认分析: {}", e.getMessage(), e);
        }

        return getDefaultQueryContext(question);
    }

    private QueryContext parseQueryContextFromLlm(String llmResponse) {
        QueryContext context = new QueryContext();

        try {
            Map<String, Object> result = JSON.parseObject(llmResponse, Map.class);
            context.setIntent((String) result.get("intent"));
            context.setQueryType((String) result.get("query_type"));
            context.setComplexity((String) result.get("complexity"));
            log.info("解析查询上下文成功: intent={}, queryType={}, complexity={}",
                    context.getIntent(), context.getQueryType(), context.getComplexity());
        } catch (Exception e) {
            log.warn("解析LLM响应失败: {}", e.getMessage());
        }

        return context;
    }

    private QueryContext getDefaultQueryContext(String question) {
        QueryContext context = new QueryContext();

        if (question.contains("查询") || question.contains("查看") || question.contains("显示")) {
            context.setIntent("query_data");
            context.setQueryType("single_table");
        } else if (question.contains("比较") || question.contains("对比")) {
            context.setIntent("compare_data");
            context.setQueryType("multi_table");
        } else if (question.contains("趋势") || question.contains("变化")) {
            context.setIntent("analyze_trend");
            context.setQueryType("aggregation");
        } else {
            context.setIntent("query_data");
            context.setQueryType("single_table");
        }

        context.setComplexity(question.length() > 50 ? "complex" : "simple");
        return context;
    }

    private String generateSql(String question, QueryContext context, String vehicleContext) {
        log.info("生成SQL: question={}, vehicleContext={}", question, vehicleContext);

        try {
            String prompt = qaPromptService.buildSqlGenerationPrompt(question, null, vehicleContext, "");
            log.debug("SQL生成Prompt: {}", prompt);

            String generatedSql = qaLlmService.generateSql(prompt);
            log.info("LLM生成的SQL: {}", generatedSql);

            if (generatedSql == null || generatedSql.trim().isEmpty()) {
                log.warn("LLM生成的SQL为空，使用默认SQL");
                return getDefaultSql(question);
            }

            return generatedSql.trim();
        } catch (Exception e) {
            log.error("调用LLM生成SQL失败，使用默认SQL: {}", e.getMessage(), e);
            return getDefaultSql(question);
        }
    }

    private String getDefaultSql(String question) {
        if (question.contains("功能域") && question.contains("平均分")) {
            return "SELECT domain_id, AVG(score) as avg_score FROM vehicle_domain_score WHERE type = 1 GROUP BY domain_id";
        } else if (question.contains("车辆") && question.contains("评分")) {
            return "SELECT vehicle_id, score FROM vehicle_domain_score WHERE type = 1 LIMIT 10";
        }
        return "SELECT * FROM vehicle_domain_score LIMIT 5";
    }

    private String retrieveVehicleContext(String question) {
        log.info("检索车辆上下文: question={}", question);

        try {
            var vehicleDocs = qaKnowledgeBaseService.searchVehicleInfo(question);
            if (vehicleDocs == null || vehicleDocs.isEmpty()) {
                log.debug("未检索到车辆信息");
                return "";
            }

            StringBuilder contextBuilder = new StringBuilder();
            contextBuilder.append("可用车辆信息：\n");

            for (var doc : vehicleDocs) {
                contextBuilder.append("- ").append(doc.getContent()).append("\n");
            }

            String context = contextBuilder.toString();
            log.info("检索到车辆上下文: {}", context);
            return context;
        } catch (Exception e) {
            log.error("检索车辆上下文失败", e);
            return "";
        }
    }

    private String generateAnswer(String question, Object queryResult, QueryContext context) {
        log.info("生成答案: question={}", question);

        try {
            String videoContext = "";

            if (videoVectorSearchService.needsVideoSearch(question)) {
                log.info("问题需要视频检索，开始向量检索");
                videoContext = buildVideoVectorContext(question);
            } else if (isVideoAnalysisQuestion(question) && queryResult instanceof List) {
                videoContext = buildVideoAnalysisContext(question, (List<?>) queryResult);
            }

            String prompt = qaPromptService.buildAnswerGenerationPrompt(question, queryResult, null, null,
                    videoContext);
            log.debug("答案生成Prompt: {}", prompt);

            String answer = qaLlmService.generateAnswer(prompt);
            log.info("LLM生成的答案: {}", answer);

            if (answer == null || answer.trim().isEmpty()) {
                log.warn("LLM生成的答案为空，使用默认答案");
                return getDefaultAnswer(question, queryResult);
            }

            return answer.trim();
        } catch (Exception e) {
            log.error("调用LLM生成答案失败，使用默认答案: {}", e.getMessage(), e);
            return getDefaultAnswer(question, queryResult);
        }
    }

    private String buildVideoVectorContext(String question) {
        log.info("构建视频向量检索上下文: question={}", question);

        try {
            List<VideoVectorSearchResult> searchResults = videoVectorSearchService.hybridSearch(question, 5);

            if (searchResults == null || searchResults.isEmpty()) {
                log.info("向量检索未找到相关视频");
                return "";
            }

            log.info("向量检索找到{}个相关视频", searchResults.size());

            return videoVectorSearchService.buildSearchContext(searchResults);

        } catch (Exception e) {
            log.error("构建视频向量检索上下文失败: {}", e.getMessage(), e);
            return "";
        }
    }

    private boolean isVideoAnalysisQuestion(String question) {
        String lowerQuestion = question.toLowerCase();
        return lowerQuestion.contains("视频") ||
                lowerQuestion.contains("表现") ||
                lowerQuestion.contains("演示") ||
                lowerQuestion.contains("分析视频");
    }

    private String buildVideoAnalysisContext(String question, List<?> queryResult) {
        log.info("构建视频分析上下文");

        StringBuilder contextBuilder = new StringBuilder();
        contextBuilder.append("\n\n## 视频分析结果\n");

        try {
            List<Long> videoIds = extractVideoIds(queryResult);

            if (videoIds.isEmpty()) {
                return "";
            }

            int analyzedCount = 0;
            for (Long videoId : videoIds) {
                if (videoAnalysisService.isVideoAnalyzed(videoId)) {
                    var result = videoAnalysisService.getAnalysisResult(videoId);
                    if (result != null) {
                        contextBuilder.append(String.format("\n### 视频ID: %d\n", videoId));
                        contextBuilder.append(String.format("- 总分: %.2f\n", result.getOverallScore()));
                        contextBuilder.append(String.format("- 总结: %s\n", result.getSummary()));

                        if (result.getDimensions() != null && !result.getDimensions().isEmpty()) {
                            contextBuilder.append("- 各维度评分:\n");
                            result.getDimensions().forEach((key, value) -> {
                                contextBuilder.append(String.format("  - %s: %.2f (%s)\n",
                                        key, value.getScore(), value.getComment()));
                            });
                        }

                        if (result.getImprovementSuggestions() != null
                                && !result.getImprovementSuggestions().isEmpty()) {
                            contextBuilder.append("- 改进建议: ")
                                    .append(String.join("; ", result.getImprovementSuggestions())).append("\n");
                        }

                        analyzedCount++;
                    }
                }
            }

            if (analyzedCount == 0) {
                contextBuilder.append("\n注意: 相关视频尚未完成分析，请先进行视频分析。\n");
            }

            return contextBuilder.toString();
        } catch (Exception e) {
            log.error("构建视频分析上下文失败: {}", e.getMessage(), e);
            return "";
        }
    }

    private List<Long> extractVideoIds(List<?> queryResult) {
        List<Long> videoIds = new ArrayList<>();

        for (Object item : queryResult) {
            if (item instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) item;
                Object videoId = map.get("id");
                if (videoId != null) {
                    videoIds.add(((Number) videoId).longValue());
                }
            }
        }

        return videoIds;
    }

    private String getDefaultAnswer(String question, Object queryResult) {
        StringBuilder answer = new StringBuilder();

        if (queryResult instanceof List && !((List<?>) queryResult).isEmpty()) {
            List<?> results = (List<?>) queryResult;
            answer.append("根据查询结果，找到").append(results.size()).append("条记录。");

            if (question.contains("平均分")) {
                answer.append("以下是功能域平均分数据：");
                results.forEach(item -> {
                    if (item instanceof Map) {
                        Map<?, ?> map = (Map<?, ?>) item;
                        Object score = map.get("avg_score");
                        if (score instanceof Number) {
                            BigDecimal bd = new BigDecimal(score.toString());
                            answer.append(" 平均分：").append(bd.setScale(2, RoundingMode.HALF_UP));
                        }
                    }
                });
            }
        } else {
            answer.append("未找到相关数据，请检查查询条件。");
        }

        return answer.toString();
    }

    private QaQueryHistoryEntity saveQueryHistory(QaQueryRequest request, String sql,
            Object queryResult, String answer,
            String vizType, long startTime) {
        QaQueryHistoryEntity history = new QaQueryHistoryEntity();
        history.setSessionId(request.getSessionId());
        history.setUserId(request.getUserId());
        history.setQuestion(request.getQuestion());
        history.setGeneratedSql(sql);
        history.setSqlResult(JSON.toJSONString(queryResult));
        history.setAnswer(answer);
        history.setVisualizationType(vizType);
        history.setStatus("success");
        history.setResponseTime((int) (System.currentTimeMillis() - startTime));
        history.setIsCacheHit(false);
        history.setCreateTime(LocalDateTime.now());

        qaQueryHistoryDao.insert(history);
        return history;
    }

    private void saveFailedQueryHistory(QaQueryRequest request, String errorMessage, long startTime) {
        QaQueryHistoryEntity history = new QaQueryHistoryEntity();
        history.setSessionId(request.getSessionId());
        history.setUserId(request.getUserId());
        history.setQuestion(request.getQuestion());
        history.setStatus("failed");
        history.setErrorMessage(errorMessage);
        history.setResponseTime((int) (System.currentTimeMillis() - startTime));
        history.setCreateTime(LocalDateTime.now());

        qaQueryHistoryDao.insert(history);
    }

    private QaQueryResponse buildResponse(String queryId, QueryContext context, String answer,
            String sql, Object queryResult, String vizType,
            Object vizData, long startTime) {
        return QaQueryResponse.builder()
                .queryId(queryId)
                .responseType(context != null ? context.getIntent() : "query_data")
                .confidenceScore(0.85)
                .answer(QaQueryResponse.AnswerInfo.builder()
                        .text(answer)
                        .dataSummary("查询完成")
                        .build())
                .queryInfo(QaQueryResponse.QueryInfo.builder()
                        .sqlGenerated(sql)
                        .recordCount(getRecordCount(queryResult))
                        .build())
                .visualization(QaQueryResponse.VisualizationInfo.builder()
                        .recommendedType(vizType)
                        .dataStructure(vizData)
                        .build())
                .processingTime(System.currentTimeMillis() - startTime)
                .build();
    }

    private int getRecordCount(Object result) {
        if (result instanceof List) {
            return ((List<?>) result).size();
        }
        return 0;
    }

    private boolean isSensitiveQuery(String question) {
        return question.toLowerCase().contains("密码") ||
                question.toLowerCase().contains("敏感") ||
                question.toLowerCase().contains("private");
    }

    private String getClientIp() {
        // 实际实现中从请求上下文中获取
        return "127.0.0.1";
    }

    private String getUserAgent() {
        // 实际实现中从请求上下文中获取
        return "Mozilla/5.0";
    }

    /**
     * 查询上下文内部类
     */
    private static class QueryContext {
        private String intent;
        private String queryType;
        private String complexity;
        private Long templateId;

        // Getters and setters
        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public String getQueryType() {
            return queryType;
        }

        public void setQueryType(String queryType) {
            this.queryType = queryType;
        }

        public String getComplexity() {
            return complexity;
        }

        public void setComplexity(String complexity) {
            this.complexity = complexity;
        }

        public Long getTemplateId() {
            return templateId;
        }

        public void setTemplateId(Long templateId) {
            this.templateId = templateId;
        }
    }
}
