package com.xailab.vehicle.xaivehicledata.service;

import com.alibaba.fastjson.JSON;
import com.xailab.vehicle.xaivehicledata.entity.QaQuestionTemplateEntity;
import com.xailab.vehicle.xaivehicledata.entity.QaVisualizationTemplateEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 智能问答系统数据初始化器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QaDataInitializer implements CommandLineRunner {

    private final QaTemplateService qaTemplateService;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化智能问答系统数据...");

        try {
            initializeQuestionTemplates();
            initializeVisualizationTemplates();
            log.info("智能问答系统数据初始化完成");
        } catch (Exception e) {
            log.error("智能问答系统数据初始化失败", e);
        }
    }

    /**
     * 初始化问题模板。
     * 车辆数据来自 vehicle_brand（品牌）+ vehicle_base_info（型号），无 vehicle_info 表；
     * vehicle_id 即 vehicle_base_info.id。建议将车辆列表（id 与「品牌+型号」对应）同步到
     * qa_knowledge_base 或作为 prompt 上下文，便于模型从「比较小米SU7和蔚来ET5」等自然语言中解析出正确的 vehicleIds。
     */
    private void initializeQuestionTemplates() {
        List<QaQuestionTemplateEntity> defaultTemplates = Arrays.asList(
                // 功能域查询模板
                createTemplate(
                        "功能域平均分查询",
                        "查询所有功能域的平均评分",
                        "function_domain",
                        "查询.*功能域.*平均分|功能域.*分",
                        Arrays.asList("功能域", "平均分", "评分"),
                        "SELECT domain_id, AVG(score) as avg_score FROM vehicle_domain_score WHERE type = 1 GROUP BY domain_id",
                        "table",
                        1),

                // 单车功能域详情
                createTemplate(
                        "单车功能域详情",
                        "查询特定车辆的功能域评分详情",
                        "function_domain",
                        "查询.*车.*功能域|车辆.*功能域.*分",
                        Arrays.asList("车辆", "功能域", "评分", "详情"),
                        "SELECT * FROM vehicle_domain_score WHERE vehicle_id = #{vehicleId} AND type = 1",
                        "bar",
                        2),

                // 多车对比分析（车辆来自 vehicle_brand + vehicle_base_info：品牌在 brand 字段，型号在
                // vehicle_model，通过 brand_id 关联）
                createTemplate(
                        "多车对比分析",
                        "比较多辆车的功能域评分",
                        "comparison",
                        "比较.*车.*功能域|多车.*对比|车辆.*对比",
                        Arrays.asList("比较", "车辆", "功能域", "对比"),
                        "SELECT CONCAT(b.brand, base.vehicle_model) AS vehicle_name, d.index_name AS domain_name, s.score FROM vehicle_domain_score s JOIN vehicle_base_info base ON s.vehicle_id = base.id JOIN vehicle_brand b ON base.brand_id = b.id JOIN vehicle_domain_index d ON s.domain_id = d.id WHERE s.vehicle_id IN (${vehicleIds}) AND s.type = 1",
                        "radar",
                        3),

                // 趋势分析
                createTemplate(
                        "评分趋势分析",
                        "分析评分随时间的变化趋势",
                        "trend",
                        "趋势.*分析|变化.*趋势|随时间.*变化",
                        Arrays.asList("趋势", "分析", "变化", "时间"),
                        "SELECT DATE(create_time) as date, AVG(score) as avg_score FROM vehicle_domain_score WHERE create_time >= '${startDate}' GROUP BY DATE(create_time) ORDER BY date",
                        "line",
                        4),

                // 统计汇总
                createTemplate(
                        "评分统计汇总",
                        "提供评分的统计汇总信息",
                        "statistics",
                        "统计.*汇总|汇总.*统计|总计|总体.*情况",
                        Arrays.asList("统计", "汇总", "总计", "总体"),
                        "SELECT COUNT(*) as total_records, AVG(score) as avg_score, MIN(score) as min_score, MAX(score) as max_score, STD(score) as std_score FROM vehicle_domain_score WHERE type = 1",
                        "gauge",
                        5),

                // 开源题库查询（车辆来自 vehicle_brand + vehicle_base_info）
                createTemplate(
                        "开源题库评分查询",
                        "查询开源题库的评分情况",
                        "open_source",
                        "开源.*题|题库.*分|开源题库",
                        Arrays.asList("开源", "题库", "评分"),
                        "SELECT CONCAT(b.brand, base.vehicle_model) AS vehicle_name, COUNT(*) AS total_cases, AVG(s.score) AS avg_score FROM beeeval_open_case_score s JOIN vehicle_base_info base ON s.vehicle_id = base.id JOIN vehicle_brand b ON base.brand_id = b.id GROUP BY s.vehicle_id",
                        "bar",
                        6));

        for (QaQuestionTemplateEntity template : defaultTemplates) {
            try {
                // 检查是否已存在
                if (qaTemplateService.searchTemplates(template.getName()).isEmpty()) {
                    qaTemplateService.createTemplate(convertToRequest(template));
                    log.info("创建默认模板: {}", template.getName());
                }
            } catch (Exception e) {
                log.warn("创建模板失败: {}, 错误: {}", template.getName(), e.getMessage());
            }
        }
    }

    /**
     * 初始化可视化模板
     */
    private void initializeVisualizationTemplates() {
        // 这里可以初始化一些默认的可视化配置
        // 暂时留空，后续根据需要添加
    }

    /**
     * 创建模板实体
     */
    private QaQuestionTemplateEntity createTemplate(String name, String description, String category,
            String pattern, List<String> keywords, String sql,
            String vizType, int priority) {
        QaQuestionTemplateEntity template = new QaQuestionTemplateEntity();
        template.setName(name);
        template.setDescription(description);
        template.setCategory(category);
        template.setQuestionPattern(pattern);
        template.setKeywords(JSON.toJSONString(keywords));
        template.setSqlTemplate(sql);
        template.setVisualizationType(vizType);
        template.setPriority(priority);
        template.setIsActive(true);
        template.setHitCount(0);
        template.setCreateUser("system");
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());

        return template;
    }

    /**
     * 转换为请求对象
     */
    private com.xailab.vehicle.xaivehicledata.entity.request.QaTemplateRequest convertToRequest(
            QaQuestionTemplateEntity entity) {
        com.xailab.vehicle.xaivehicledata.entity.request.QaTemplateRequest request = new com.xailab.vehicle.xaivehicledata.entity.request.QaTemplateRequest();
        request.setName(entity.getName());
        request.setDescription(entity.getDescription());
        request.setCategory(entity.getCategory());
        request.setQuestionPattern(entity.getQuestionPattern());
        request.setKeywords(entity.getKeywords());
        request.setSqlTemplate(entity.getSqlTemplate());
        request.setVisualizationType(entity.getVisualizationType());
        request.setPriority(entity.getPriority());
        request.setCreateUser(entity.getCreateUser());
        return request;
    }
}
