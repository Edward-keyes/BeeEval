package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.QaVisualizationTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 可视化模板DAO接口
 */
@Mapper
public interface QaVisualizationTemplateDao extends BaseMapper<QaVisualizationTemplateEntity> {

    /**
     * 获取所有活跃的可视化模板
     */
    @Select("SELECT * FROM qa_visualization_template WHERE is_active = true ORDER BY is_default DESC, name ASC")
    List<QaVisualizationTemplateEntity> selectActiveTemplates();

    /**
     * 根据类型获取模板
     */
    @Select("SELECT * FROM qa_visualization_template WHERE type = #{type} AND is_active = true ORDER BY is_default DESC")
    List<QaVisualizationTemplateEntity> selectByType(@Param("type") String type);

    /**
     * 获取默认模板
     */
    @Select("SELECT * FROM qa_visualization_template WHERE is_default = true AND is_active = true")
    List<QaVisualizationTemplateEntity> selectDefaultTemplates();

    /**
     * 根据分类获取模板
     */
    @Select("SELECT * FROM qa_visualization_template WHERE category = #{category} AND is_active = true ORDER BY name ASC")
    List<QaVisualizationTemplateEntity> selectByCategory(@Param("category") String category);
}
