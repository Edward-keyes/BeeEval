package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.xaivehicledata.dao.QaQuestionTemplateDao;
import com.xailab.vehicle.xaivehicledata.entity.QaQuestionTemplateEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.QaTemplateRequest;
import com.xailab.vehicle.xaivehicledata.entity.vo.QaTemplateVO;
import com.xailab.vehicle.xaivehicledata.service.QaTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 问题模板服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaTemplateServiceImpl implements QaTemplateService {

    private final QaQuestionTemplateDao qaQuestionTemplateDao;

    // 相似度计算工具
    private final JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    // 缓存活跃模板
    private volatile List<QaQuestionTemplateEntity> activeTemplates;

    @PostConstruct
    public void init() {
        // 预加载活跃模板
        refreshActiveTemplates();
        log.info("问题模板服务已初始化，加载了{}个活跃模板", activeTemplates.size());
    }

    @Override
    public List<QaTemplateVO> getTemplateList(String category) {
        List<QaQuestionTemplateEntity> templates;

        if (StringUtils.hasText(category)) {
            templates = qaQuestionTemplateDao.selectByCategory(category);
        } else {
            templates = qaQuestionTemplateDao.selectActiveTemplates();
        }

        return templates.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public QaTemplateVO getTemplateDetail(Long templateId) {
        QaQuestionTemplateEntity entity = qaQuestionTemplateDao.selectById(templateId);
        return entity != null ? convertToVO(entity) : null;
    }

    @Override
    @Transactional
    public QaTemplateVO createTemplate(QaTemplateRequest request) {
        QaQuestionTemplateEntity entity = new QaQuestionTemplateEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setIsActive(true);
        entity.setHitCount(0);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());

        // 解析关键词
        if (StringUtils.hasText(request.getKeywords())) {
            entity.setKeywords(request.getKeywords());
        }

        // 解析参数配置
        if (StringUtils.hasText(request.getParameters())) {
            entity.setParameters(request.getParameters());
        }

        // 解析可视化配置
        if (StringUtils.hasText(request.getVisualizationConfig())) {
            entity.setVisualizationConfig(request.getVisualizationConfig());
        }

        qaQuestionTemplateDao.insert(entity);

        // 插入后重新查询，确保所有字段（特别是 create_time）都被正确填充
        QaQuestionTemplateEntity savedEntity = qaQuestionTemplateDao.selectById(entity.getId());
        if (savedEntity == null) {
            throw new RuntimeException("创建模板失败：无法查询到刚插入的记录");
        }

        // 刷新缓存
        refreshActiveTemplates();

        log.info("创建问题模板成功: {}", savedEntity.getName());
        return convertToVO(savedEntity);
    }

    @Override
    @Transactional
    public QaTemplateVO updateTemplate(Long templateId, QaTemplateRequest request) {
        QaQuestionTemplateEntity entity = qaQuestionTemplateDao.selectById(templateId);
        if (entity == null) {
            throw new IllegalArgumentException("模板不存在: " + templateId);
        }

        BeanUtils.copyProperties(request, entity, "id", "createTime", "hitCount");
        entity.setUpdateTime(new Date());

        // 解析关键词
        if (StringUtils.hasText(request.getKeywords())) {
            entity.setKeywords(request.getKeywords());
        }

        // 解析参数配置
        if (StringUtils.hasText(request.getParameters())) {
            entity.setParameters(request.getParameters());
        }

        // 解析可视化配置
        if (StringUtils.hasText(request.getVisualizationConfig())) {
            entity.setVisualizationConfig(request.getVisualizationConfig());
        }

        qaQuestionTemplateDao.updateById(entity);

        // 刷新缓存
        refreshActiveTemplates();

        log.info("更新问题模板成功: {}", entity.getName());
        return convertToVO(entity);
    }

    @Override
    @Transactional
    public void deleteTemplate(Long templateId) {
        QaQuestionTemplateEntity entity = qaQuestionTemplateDao.selectById(templateId);
        if (entity == null) {
            throw new IllegalArgumentException("模板不存在: " + templateId);
        }

        qaQuestionTemplateDao.deleteById(templateId);

        // 刷新缓存
        refreshActiveTemplates();

        log.info("删除问题模板成功: {}", entity.getName());
    }

    @Override
    public QaTemplateVO matchTemplate(String question) {
        if (!StringUtils.hasText(question)) {
            return null;
        }

        List<QaQuestionTemplateEntity> candidates = getActiveTemplates();

        // 多层匹配策略
        TemplateMatchResult result = performMultiLevelMatching(question, candidates);

        if (result != null && result.getTemplate() != null) {
            // 异步更新命中次数
            updateHitCountAsync(result.getTemplate().getId());

            log.debug("模板匹配成功: question={}, template={}, matchType={}",
                    question, result.getTemplate().getName(), result.getMatchType());
            return convertToVO(result.getTemplate());
        }

        log.debug("模板匹配失败: question={}", question);
        return null;
    }

    @Override
    public List<QaTemplateVO> getPopularTemplates(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        List<QaQuestionTemplateEntity> templates = qaQuestionTemplateDao.selectPopularTemplates(limit);

        return templates.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<QaTemplateVO> searchTemplates(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }

        List<QaQuestionTemplateEntity> templates = qaQuestionTemplateDao.searchByKeyword(keyword.trim());

        return templates.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 执行多层模板匹配
     */
    private TemplateMatchResult performMultiLevelMatching(String question, List<QaQuestionTemplateEntity> candidates) {
        // 1. 精确关键词匹配
        TemplateMatchResult exactMatch = performExactKeywordMatching(question, candidates);
        if (exactMatch != null) {
            return exactMatch;
        }

        // 2. 模糊匹配
        TemplateMatchResult fuzzyMatch = performFuzzyMatching(question, candidates);
        if (fuzzyMatch != null) {
            return fuzzyMatch;
        }

        // 3. 正则匹配
        TemplateMatchResult regexMatch = performRegexMatching(question, candidates);
        if (regexMatch != null) {
            return regexMatch;
        }

        return null;
    }

    /**
     * 精确关键词匹配
     */
    private TemplateMatchResult performExactKeywordMatching(String question,
            List<QaQuestionTemplateEntity> candidates) {
        String normalizedQuestion = normalizeText(question);

        for (QaQuestionTemplateEntity template : candidates) {
            if (!StringUtils.hasText(template.getKeywords())) {
                continue;
            }

            try {
                List<String> keywords = JSON.parseArray(template.getKeywords(), String.class);
                if (keywords.stream().allMatch(keyword -> normalizedQuestion.contains(normalizeText(keyword)))) {
                    return new TemplateMatchResult(template, MatchType.EXACT, 1.0);
                }
            } catch (Exception e) {
                log.warn("解析模板关键词失败: templateId={}, keywords={}",
                        template.getId(), template.getKeywords());
            }
        }

        return null;
    }

    /**
     * 模糊匹配
     */
    private TemplateMatchResult performFuzzyMatching(String question, List<QaQuestionTemplateEntity> candidates) {
        String normalizedQuestion = normalizeText(question);

        List<TemplateScore> scores = new ArrayList<>();

        for (QaQuestionTemplateEntity template : candidates) {
            double score = calculateTemplateScore(normalizedQuestion, template);
            if (score >= 0.6) { // 相似度阈值
                scores.add(new TemplateScore(template, score));
            }
        }

        if (!scores.isEmpty()) {
            scores.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
            TemplateScore bestMatch = scores.get(0);
            return new TemplateMatchResult(bestMatch.getTemplate(), MatchType.FUZZY, bestMatch.getScore());
        }

        return null;
    }

    /**
     * 正则匹配
     */
    private TemplateMatchResult performRegexMatching(String question, List<QaQuestionTemplateEntity> candidates) {
        for (QaQuestionTemplateEntity template : candidates) {
            if (!StringUtils.hasText(template.getQuestionPattern())) {
                continue;
            }

            try {
                Pattern pattern = Pattern.compile(template.getQuestionPattern(), Pattern.CASE_INSENSITIVE);
                if (pattern.matcher(question).find()) {
                    return new TemplateMatchResult(template, MatchType.REGEX, 0.9);
                }
            } catch (Exception e) {
                log.warn("编译正则表达式失败: templateId={}, pattern={}",
                        template.getId(), template.getQuestionPattern());
            }
        }

        return null;
    }

    /**
     * 计算模板匹配分数
     */
    private double calculateTemplateScore(String question, QaQuestionTemplateEntity template) {
        double score = 0.0;

        // 1. 关键词相似度 (40%)
        double keywordScore = calculateKeywordSimilarity(question, template);
        score += keywordScore * 0.4;

        // 2. 描述相似度 (30%)
        if (StringUtils.hasText(template.getDescription())) {
            double descScore = jaccardSimilarity.apply(question, template.getDescription());
            score += descScore * 0.3;
        }

        // 3. 使用频率权重 (20%)
        double frequencyScore = Math.min(template.getHitCount() / 1000.0, 1.0); // 归一化
        score += frequencyScore * 0.2;

        // 4. 优先级权重 (10%)
        double priorityScore = template.getPriority() != null ? Math.min(template.getPriority() / 10.0, 1.0) : 0.0;
        score += priorityScore * 0.1;

        return Math.min(score, 1.0);
    }

    /**
     * 计算关键词相似度
     */
    private double calculateKeywordSimilarity(String question, QaQuestionTemplateEntity template) {
        if (!StringUtils.hasText(template.getKeywords())) {
            return 0.0;
        }

        try {
            List<String> keywords = JSON.parseArray(template.getKeywords(), String.class);
            double totalScore = 0.0;

            for (String keyword : keywords) {
                String normalizedKeyword = normalizeText(keyword);
                if (question.contains(normalizedKeyword)) {
                    totalScore += 1.0;
                } else {
                    // 计算编辑距离相似度
                    int distance = levenshteinDistance.apply(question, normalizedKeyword);
                    double similarity = 1.0
                            - (double) distance / Math.max(question.length(), normalizedKeyword.length());
                    totalScore += Math.max(similarity, 0.0);
                }
            }

            return totalScore / keywords.size();
        } catch (Exception e) {
            log.warn("计算关键词相似度失败: {}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * 标准化文本
     */
    private String normalizeText(String text) {
        if (!StringUtils.hasText(text)) {
            return "";
        }

        return text.toLowerCase()
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[\\p{Punct}&&[^，。？！；：\"'（）【】《》]]", "");
    }

    /**
     * 获取活跃模板
     */
    private List<QaQuestionTemplateEntity> getActiveTemplates() {
        if (activeTemplates == null) {
            refreshActiveTemplates();
        }
        return activeTemplates;
    }

    /**
     * 刷新活跃模板缓存
     */
    private synchronized void refreshActiveTemplates() {
        activeTemplates = qaQuestionTemplateDao.selectActiveTemplates();
        log.debug("刷新活跃模板缓存: {}个模板", activeTemplates.size());
    }

    /**
     * 异步更新命中次数
     */
    private void updateHitCountAsync(Long templateId) {
        // 这里可以改为异步执行
        try {
            qaQuestionTemplateDao.incrementHitCount(templateId);
        } catch (Exception e) {
            log.warn("更新模板命中次数失败: templateId={}, error={}", templateId, e.getMessage());
        }
    }

    /**
     * 转换为VO
     */
    private QaTemplateVO convertToVO(QaQuestionTemplateEntity entity) {
        QaTemplateVO vo = new QaTemplateVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    /**
     * 匹配结果内部类
     */
    private static class TemplateMatchResult {
        private QaQuestionTemplateEntity template;
        private MatchType matchType;
        private double confidence;

        public TemplateMatchResult(QaQuestionTemplateEntity template, MatchType matchType, double confidence) {
            this.template = template;
            this.matchType = matchType;
            this.confidence = confidence;
        }

        // Getters
        public QaQuestionTemplateEntity getTemplate() {
            return template;
        }

        public MatchType getMatchType() {
            return matchType;
        }

        public double getConfidence() {
            return confidence;
        }
    }

    /**
     * 匹配类型枚举
     */
    private enum MatchType {
        EXACT, FUZZY, REGEX, NONE
    }

    /**
     * 模板评分内部类
     */
    private static class TemplateScore {
        private QaQuestionTemplateEntity template;
        private double score;

        public TemplateScore(QaQuestionTemplateEntity template, double score) {
            this.template = template;
            this.score = score;
        }

        // Getters
        public QaQuestionTemplateEntity getTemplate() {
            return template;
        }

        public double getScore() {
            return score;
        }
    }
}