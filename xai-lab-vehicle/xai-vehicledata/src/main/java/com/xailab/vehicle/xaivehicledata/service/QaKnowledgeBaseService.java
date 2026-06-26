package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.QaKnowledgeBaseEntity;

import java.util.List;

/**
 * 知识库检索服务接口
 * 用于从qa_knowledge_base表中检索相关文档
 */
public interface QaKnowledgeBaseService {

    /**
     * 根据问题检索相关文档（基于关键词匹配）
     * 
     * @param question 用户问题
     * @param limit    返回的最大文档数
     * @return 相关文档列表
     */
    List<QaKnowledgeBaseEntity> searchRelevantDocuments(String question, int limit);

    /**
     * 从问题中提取关键词
     * 
     * @param question 用户问题
     * @return 关键词列表
     */
    List<String> extractKeywords(String question);

    /**
     * 根据分类获取文档
     * 
     * @param category 分类
     * @return 文档列表
     */
    List<QaKnowledgeBaseEntity> getDocumentsByCategory(String category);

    /**
     * 根据标签获取文档
     * 
     * @param tag 标签
     * @return 文档列表
     */
    List<QaKnowledgeBaseEntity> getDocumentsByTag(String tag);

    /**
     * 检索车辆信息（专门用于车辆数据检索）
     * 
     * @param question 用户问题
     * @return 车辆信息列表
     */
    List<QaKnowledgeBaseEntity> searchVehicleInfo(String question);
}
