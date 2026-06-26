package com.xailab.vehicle.xaivehicledata.service;

import java.util.List;
import java.util.Map;

/**
 * 可视化服务接口
 */
public interface QaVisualizationService {

    /**
     * 智能推荐图表类型
     */
    String recommendChartType(Object data, String queryIntent);

    /**
     * 生成图表配置
     */
    Map<String, Object> generateChartConfig(Object data, String chartType);

    /**
     * 获取可视化模板列表
     */
    List<VisualizationTemplate> getVisualizationTemplates(String category);

    /**
     * 获取可视化模板详情
     */
    VisualizationTemplate getVisualizationTemplate(String type);

    /**
     * 数据格式转换
     */
    Object transformDataForVisualization(Object rawData, String chartType);

    /**
     * 导出可视化数据
     */
    byte[] exportVisualizationData(Object data, String format);

    /**
     * 可视化模板类
     */
    class VisualizationTemplate {
        private String name;
        private String type;
        private Map<String, Object> config;
        private String description;
        private String category;
        private boolean isDefault;

        public VisualizationTemplate(String name, String type, Map<String, Object> config,
                                   String description, String category, boolean isDefault) {
            this.name = name;
            this.type = type;
            this.config = config;
            this.description = description;
            this.category = category;
            this.isDefault = isDefault;
        }

        // Getters
        public String getName() { return name; }
        public String getType() { return type; }
        public Map<String, Object> getConfig() { return config; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public boolean isDefault() { return isDefault; }
    }
}
