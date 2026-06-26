package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.xaivehicledata.entity.request.QaTemplateRequest;
import com.xailab.vehicle.xaivehicledata.entity.vo.QaTemplateVO;

import java.util.List;

/**
 * 问题模板服务接口
 */
public interface QaTemplateService {

    /**
     * 获取模板列表
     */
    List<QaTemplateVO> getTemplateList(String category);

    /**
     * 获取模板详情
     */
    QaTemplateVO getTemplateDetail(Long templateId);

    /**
     * 创建模板
     */
    QaTemplateVO createTemplate(QaTemplateRequest request);

    /**
     * 更新模板
     */
    QaTemplateVO updateTemplate(Long templateId, QaTemplateRequest request);

    /**
     * 删除模板
     */
    void deleteTemplate(Long templateId);

    /**
     * 匹配问题模板
     */
    QaTemplateVO matchTemplate(String question);

    /**
     * 获取热门模板
     */
    List<QaTemplateVO> getPopularTemplates(Integer limit);

    /**
     * 搜索模板
     */
    List<QaTemplateVO> searchTemplates(String keyword);
}
