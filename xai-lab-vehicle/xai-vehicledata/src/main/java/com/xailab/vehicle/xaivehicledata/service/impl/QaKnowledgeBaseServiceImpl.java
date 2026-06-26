package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xailab.vehicle.xaivehicledata.dao.QaKnowledgeBaseDao;
import com.xailab.vehicle.xaivehicledata.entity.QaKnowledgeBaseEntity;
import com.xailab.vehicle.xaivehicledata.service.QaKnowledgeBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 知识库检索服务实现
 * 基于关键词匹配实现文档检索
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaKnowledgeBaseServiceImpl implements QaKnowledgeBaseService {

    private final QaKnowledgeBaseDao qaKnowledgeBaseDao;

    private static final String VEHICLE_CATEGORY = "vehicle";
    private static final int DEFAULT_LIMIT = 5;

    @Override
    public List<QaKnowledgeBaseEntity> searchRelevantDocuments(String question, int limit) {
        log.info("检索相关文档: question={}, limit={}", question, limit);

        List<String> keywords = extractKeywords(question);
        if (keywords.isEmpty()) {
            log.warn("未能提取到关键词");
            return List.of();
        }

        List<QaKnowledgeBaseEntity> allDocuments = qaKnowledgeBaseDao.selectList(new QueryWrapper<>());
        if (allDocuments == null || allDocuments.isEmpty()) {
            log.warn("知识库中没有文档");
            return List.of();
        }

        Map<QaKnowledgeBaseEntity, Integer> scoreMap = new HashMap<>();

        for (QaKnowledgeBaseEntity doc : allDocuments) {
            int score = calculateRelevanceScore(doc, keywords);
            if (score > 0) {
                scoreMap.put(doc, score);
            }
        }

        List<QaKnowledgeBaseEntity> sortedDocuments = scoreMap.entrySet().stream()
                .sorted(Map.Entry.<QaKnowledgeBaseEntity, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        log.info("检索到{}个相关文档", sortedDocuments.size());
        return sortedDocuments;
    }

    @Override
    public List<String> extractKeywords(String question) {
        if (question == null || question.trim().isEmpty()) {
            return List.of();
        }

        List<String> keywords = new ArrayList<>();

        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+|[a-zA-Z]+");
        Matcher matcher = pattern.matcher(question);

        Set<String> keywordSet = new HashSet<>();
        while (matcher.find()) {
            String word = matcher.group().trim();
            if (word.length() >= 2) {
                keywordSet.add(word);
            }
        }

        keywords.addAll(keywordSet);

        log.debug("提取关键词: question={}, keywords={}", question, keywords);
        return keywords;
    }

    @Override
    public List<QaKnowledgeBaseEntity> getDocumentsByCategory(String category) {
        log.info("根据分类获取文档: category={}", category);

        QueryWrapper<QaKnowledgeBaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("category", category);

        return qaKnowledgeBaseDao.selectList(wrapper);
    }

    @Override
    public List<QaKnowledgeBaseEntity> getDocumentsByTag(String tag) {
        log.info("根据标签获取文档: tag={}", tag);

        return qaKnowledgeBaseDao.selectByTag(tag);
    }

    @Override
    public List<QaKnowledgeBaseEntity> searchVehicleInfo(String question) {
        log.info("检索车辆信息: question={}", question);

        List<QaKnowledgeBaseEntity> vehicleDocs = getDocumentsByCategory(VEHICLE_CATEGORY);
        if (vehicleDocs == null || vehicleDocs.isEmpty()) {
            log.warn("知识库中没有车辆数据");
            return List.of();
        }

        List<String> keywords = extractKeywords(question);
        if (keywords.isEmpty()) {
            return vehicleDocs.stream().limit(DEFAULT_LIMIT).collect(Collectors.toList());
        }

        Map<QaKnowledgeBaseEntity, Integer> scoreMap = new HashMap<>();

        for (QaKnowledgeBaseEntity doc : vehicleDocs) {
            int score = calculateRelevanceScore(doc, keywords);
            if (score > 0) {
                scoreMap.put(doc, score);
            }
        }

        List<QaKnowledgeBaseEntity> sortedDocuments = scoreMap.entrySet().stream()
                .sorted(Map.Entry.<QaKnowledgeBaseEntity, Integer>comparingByValue().reversed())
                .limit(DEFAULT_LIMIT)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        log.info("检索到{}个相关车辆信息", sortedDocuments.size());
        return sortedDocuments;
    }

    private int calculateRelevanceScore(QaKnowledgeBaseEntity doc, List<String> keywords) {
        int score = 0;
        String content = (doc.getTitle() + " " + doc.getContent()).toLowerCase();

        for (String keyword : keywords) {
            String lowerKeyword = keyword.toLowerCase();

            if (doc.getTitle() != null && doc.getTitle().toLowerCase().contains(lowerKeyword)) {
                score += 3;
            }

            if (doc.getContent() != null && doc.getContent().toLowerCase().contains(lowerKeyword)) {
                score += 1;
            }

            if (doc.getTags() != null) {
                String tagsStr = doc.getTags().toLowerCase();
                if (tagsStr.contains(lowerKeyword)) {
                    score += 2;
                }
            }
        }

        return score;
    }
}
