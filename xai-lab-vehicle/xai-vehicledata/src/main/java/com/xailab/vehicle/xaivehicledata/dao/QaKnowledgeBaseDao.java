package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.QaKnowledgeBaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 知识库DAO接口
 */
@Mapper
public interface QaKnowledgeBaseDao extends BaseMapper<QaKnowledgeBaseEntity> {

    /**
     * 根据分类获取文档
     */
    @Select("SELECT * FROM qa_knowledge_base WHERE category = #{category} ORDER BY create_time DESC")
    List<QaKnowledgeBaseEntity> selectByCategory(@Param("category") String category);

    /**
     * 根据关键词搜索文档
     */
    @Select("SELECT * FROM qa_knowledge_base WHERE MATCH(title, content) AGAINST(#{keyword} IN NATURAL LANGUAGE MODE) ORDER BY create_time DESC")
    List<QaKnowledgeBaseEntity> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 获取相关文档（基于相似度）
     */
    @Select("SELECT *, MATCH(title, content) AGAINST(#{query} IN NATURAL LANGUAGE MODE) as relevance_score FROM qa_knowledge_base WHERE MATCH(title, content) AGAINST(#{query} IN NATURAL LANGUAGE MODE) ORDER BY relevance_score DESC LIMIT #{limit}")
    List<QaKnowledgeBaseEntity> selectRelevantDocuments(@Param("query") String query, @Param("limit") int limit);

    /**
     * 根据标签获取文档
     */
    @Select("SELECT * FROM qa_knowledge_base WHERE JSON_CONTAINS(tags, JSON_QUOTE(#{tag})) ORDER BY create_time DESC")
    List<QaKnowledgeBaseEntity> selectByTag(@Param("tag") String tag);

    /**
     * 获取最新文档
     */
    @Select("SELECT * FROM qa_knowledge_base ORDER BY create_time DESC LIMIT #{limit}")
    List<QaKnowledgeBaseEntity> selectLatestDocuments(@Param("limit") int limit);

    /**
     * 统计分类文档数量
     */
    @Select("SELECT category, COUNT(*) as count FROM qa_knowledge_base GROUP BY category")
    List<QaKnowledgeBaseEntity> countByCategory();
}
