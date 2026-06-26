package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.fastjson.JSON;
import com.xailab.vehicle.xaivehicledata.dao.QaVisualizationTemplateDao;
import com.xailab.vehicle.xaivehicledata.entity.QaVisualizationTemplateEntity;
import com.xailab.vehicle.xaivehicledata.service.QaVisualizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 可视化服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QaVisualizationServiceImpl implements QaVisualizationService {

    private final QaVisualizationTemplateDao qaVisualizationTemplateDao;

    @Override
    public String recommendChartType(Object data, String queryIntent) {
        if (data == null) {
            return "table";
        }

        // 分析数据特征
        DataCharacteristics characteristics = analyzeDataCharacteristics(data);

        // 根据查询意图推荐
        switch (queryIntent.toLowerCase()) {
            case "comparison":
                if (characteristics.getDimensions() <= 2) {
                    return "bar";
                } else {
                    return "table";
                }

            case "trend":
                return "line";

            case "distribution":
                return "pie";

            case "correlation":
                return "scatter";

            case "aggregation":
                return "bar";

            default:
                // 根据数据类型自动推荐
                if (characteristics.getRecordCount() > 100) {
                    return "table"; // 大数据量用表格
                } else if (characteristics.getNumericColumns() > characteristics.getCategoricalColumns()) {
                    return "line"; // 数值型数据用线图
                } else {
                    return "bar"; // 分类数据用柱状图
                }
        }
    }

    @Override
    public Map<String, Object> generateChartConfig(Object data, String chartType) {
        Map<String, Object> config = new HashMap<>();

        try {
            switch (chartType.toLowerCase()) {
                case "bar":
                    config = generateBarConfig(data);
                    break;
                case "line":
                    config = generateLineConfig(data);
                    break;
                case "pie":
                    config = generatePieConfig(data);
                    break;
                case "table":
                    config = generateTableConfig(data);
                    break;
                default:
                    config = generateDefaultConfig(data);
                    break;
            }
        } catch (Exception e) {
            log.warn("生成图表配置失败: chartType={}, error={}", chartType, e.getMessage());
            config = generateDefaultConfig(data);
        }

        return config;
    }

    @Override
    public List<VisualizationTemplate> getVisualizationTemplates(String category) {
        List<QaVisualizationTemplateEntity> entities;

        if (category != null && !category.trim().isEmpty()) {
            entities = qaVisualizationTemplateDao.selectByCategory(category);
        } else {
            entities = qaVisualizationTemplateDao.selectActiveTemplates();
        }

        return entities.stream()
                .map(this::convertToTemplate)
                .collect(Collectors.toList());
    }

    @Override
    public VisualizationTemplate getVisualizationTemplate(String type) {
        List<QaVisualizationTemplateEntity> entities = qaVisualizationTemplateDao.selectByType(type);

        if (!entities.isEmpty()) {
            // 返回第一个匹配的模板
            return convertToTemplate(entities.get(0));
        }

        return null;
    }

    @Override
    public Object transformDataForVisualization(Object rawData, String chartType) {
        // 这里简化实现，实际应该根据图表类型转换数据格式
        // 例如：将数据库结果转换为ECharts所需的数据格式

        if (rawData instanceof List) {
            List<?> dataList = (List<?>) rawData;

            switch (chartType.toLowerCase()) {
                case "pie":
                    return transformForPieChart(dataList);
                case "bar":
                case "line":
                    return transformForCartesianChart(dataList);
                case "table":
                    return transformForTable(dataList);
                default:
                    return rawData;
            }
        }

        return rawData;
    }

    @Override
    public byte[] exportVisualizationData(Object data, String format) {
        // 这里简化实现，实际应该根据格式导出数据
        // 支持：excel, csv, json, png等格式

        try {
            String jsonData = JSON.toJSONString(data);
            return jsonData.getBytes("UTF-8");
        } catch (Exception e) {
            log.error("导出可视化数据失败: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * 分析数据特征
     */
    private DataCharacteristics analyzeDataCharacteristics(Object data) {
        DataCharacteristics characteristics = new DataCharacteristics();

        if (data instanceof List) {
            List<?> dataList = (List<?>) data;
            characteristics.setRecordCount(dataList.size());

            if (!dataList.isEmpty()) {
                Object firstRecord = dataList.get(0);

                if (firstRecord instanceof Map) {
                    Map<?, ?> record = (Map<?, ?>) firstRecord;

                    int numericColumns = 0;
                    int categoricalColumns = 0;

                    for (Object value : record.values()) {
                        if (value instanceof Number) {
                            numericColumns++;
                        } else {
                            categoricalColumns++;
                        }
                    }

                    characteristics.setNumericColumns(numericColumns);
                    characteristics.setCategoricalColumns(categoricalColumns);
                    characteristics.setDimensions(record.size());
                }
            }
        }

        return characteristics;
    }

    /**
     * 生成柱状图配置
     */
    private Map<String, Object> generateBarConfig(Object data) {
        Map<String, Object> config = new HashMap<>();

        config.put("type", "bar");
        config.put("config", Map.of(
                "xAxis", Map.of("type", "category"),
                "yAxis", Map.of("type", "value"),
                "legend", Map.of("show", true),
                "tooltip", Map.of("show", true),
                "animation", true));

        return config;
    }

    /**
     * 生成线图配置
     */
    private Map<String, Object> generateLineConfig(Object data) {
        Map<String, Object> config = new HashMap<>();

        config.put("type", "line");
        config.put("config", Map.of(
                "xAxis", Map.of("type", "category"),
                "yAxis", Map.of("type", "value"),
                "legend", Map.of("show", true),
                "tooltip", Map.of("show", true),
                "smooth", true,
                "animation", true));

        return config;
    }

    /**
     * 生成饼图配置
     */
    private Map<String, Object> generatePieConfig(Object data) {
        Map<String, Object> config = new HashMap<>();

        config.put("type", "pie");
        config.put("config", Map.of(
                "legend", Map.of("show", true),
                "tooltip", Map.of("show", true),
                "animation", true,
                "label", Map.of(
                        "show", true,
                        "formatter", "{b}: {d}%")));

        return config;
    }

    /**
     * 生成表格配置
     */
    private Map<String, Object> generateTableConfig(Object data) {
        Map<String, Object> config = new HashMap<>();

        config.put("type", "table");
        config.put("config", Map.of(
                "pagination", true,
                "pageSize", 20,
                "sortable", true,
                "columns", "auto"));

        return config;
    }

    /**
     * 生成默认配置
     */
    private Map<String, Object> generateDefaultConfig(Object data) {
        Map<String, Object> config = new HashMap<>();

        config.put("type", "table");
        config.put("config", Map.of(
                "pagination", true,
                "pageSize", 20));

        return config;
    }

    /**
     * 转换为饼图数据格式
     */
    private Object transformForPieChart(List<?> dataList) {
        // 简化实现：假设数据是Map格式，第一个字段作为名称，第二个字段作为值
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object item : dataList) {
            if (item instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) item;
                Map<String, Object> pieItem = new HashMap<>();

                // 这里应该根据实际数据结构进行转换
                // 暂时返回原始数据
                pieItem.put("data", map);
                result.add(pieItem);
            }
        }

        return result;
    }

    /**
     * 转换为笛卡尔图表数据格式
     */
    private Object transformForCartesianChart(List<?> dataList) {
        // 简化实现
        return dataList;
    }

    /**
     * 转换为表格数据格式
     */
    private Object transformForTable(List<?> dataList) {
        // 表格数据直接使用原始格式
        return dataList;
    }

    /**
     * 转换为模板对象
     */
    private VisualizationTemplate convertToTemplate(QaVisualizationTemplateEntity entity) {
        Map<String, Object> configMap = null;
        try {
            if (entity.getConfig() != null) {
                configMap = JSON.parseObject(entity.getConfig(), Map.class);
            }
        } catch (Exception e) {
            log.warn("解析可视化模板配置失败: {}", e.getMessage());
            configMap = new HashMap<>();
        }

        return new VisualizationTemplate(
                entity.getName(),
                entity.getType(),
                configMap,
                entity.getDescription(),
                entity.getCategory(),
                entity.getIsDefault() != null ? entity.getIsDefault() : false);
    }

    /**
     * 数据特征内部类
     */
    private static class DataCharacteristics {
        private int recordCount;
        private int dimensions;
        private int numericColumns;
        private int categoricalColumns;

        // Getters and setters
        public int getRecordCount() {
            return recordCount;
        }

        public void setRecordCount(int recordCount) {
            this.recordCount = recordCount;
        }

        public int getDimensions() {
            return dimensions;
        }

        public void setDimensions(int dimensions) {
            this.dimensions = dimensions;
        }

        public int getNumericColumns() {
            return numericColumns;
        }

        public void setNumericColumns(int numericColumns) {
            this.numericColumns = numericColumns;
        }

        public int getCategoricalColumns() {
            return categoricalColumns;
        }

        public void setCategoricalColumns(int categoricalColumns) {
            this.categoricalColumns = categoricalColumns;
        }
    }
}
