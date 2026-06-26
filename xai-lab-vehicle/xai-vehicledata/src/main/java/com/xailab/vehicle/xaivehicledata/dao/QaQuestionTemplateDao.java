package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.QaQuestionTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 问题模板DAO接口
 */
@Mapper
public interface QaQuestionTemplateDao extends BaseMapper<QaQuestionTemplateEntity> {

    /**
     * 根据分类获取模板列表
     */
    @Select("SELECT * FROM qa_question_template WHERE category = #{category} AND is_active = true ORDER BY priority DESC, hit_count DESC")
    List<QaQuestionTemplateEntity> selectByCategory(@Param("category") String category);

    /**
     * 获取所有活跃模板
     */
    @Select("SELECT * FROM qa_question_template WHERE is_active = true ORDER BY priority DESC, hit_count DESC")
    List<QaQuestionTemplateEntity> selectActiveTemplates();

    /**
     * 根据关键词搜索模板
     */
    @Select("SELECT * FROM qa_question_template WHERE is_active = true AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')) ORDER BY priority DESC")
    List<QaQuestionTemplateEntity> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 更新模板使用次数
     */
    @Update("UPDATE qa_question_template SET hit_count = hit_count + 1 WHERE id = #{templateId}")
    void incrementHitCount(@Param("templateId") Long templateId);

    /**
     * 获取热门模板
     */
    @Select("SELECT * FROM qa_question_template WHERE is_active = true ORDER BY hit_count DESC LIMIT #{limit}")
    List<QaQuestionTemplateEntity> selectPopularTemplates(@Param("limit") int limit);
}
