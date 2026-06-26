package com.xailab.vehicle.operation.testplatform.pojo.excel;

import com.xailab.vehicle.operation.system.utils.poi.ExcelExport;
import com.xailab.vehicle.operation.system.utils.poi.ExcelImport;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName: TestCaseBathAddImportTemplate
 * @Description:
 * @author: liulin
 * @date: 2025/4/22 0:58
 */

@Data
@Schema(description = "测试用例批量导入模板")
@Accessors(chain = true)
public class TestCaseBathAddImportTemplate implements Serializable {

    /**
     * 每行序列号
     */
    @Schema(description = "每行序列号")
    @NotNull(message = "序列号不能为空")
    @ExcelImport(value = "序号",required = true)
    @ExcelExport(value = "序号",example ="1")
    private Integer dataSerial;
    /**
     * 用例内容
     */
    @Schema(description = "用例内容")
    @NotBlank(message = "用例内容不能为空")
    @ExcelImport(value = "用例内容",required = true)
    @ExcelExport(value = "用例内容",example = "模板示例")
    private String testcaseContent;

    /**
     * 一级指标
     */
    @Schema(description = "一级指标")
    @ExcelImport(value = "一级指标")
    @ExcelExport(value = "一级指标",example = "模板示例")
    private String primaryMetric;

    /**
     * 二级指标
     */
    @Schema(description = "二级指标")
    @ExcelImport(value = "二级指标")
    @ExcelExport(value = "二级指标",example = "模板示例")
    private String secondaryMetric;

    /**
     * 三级指标
     */
    @Schema(description = "三级指标")
    @NotBlank(message = "三级指标不能为空")
    @ExcelImport(value = "三级指标",required = true)
    @ExcelExport(value = "三级指标",example = "模板示例")
    private String tertiaryMetric;

    /**
     * 功能域id
     */
    @Schema(description = "功能域id")
    @NotNull(message = "功能域id不能为空")
    @ExcelImport(value = "功能域id",required = true)
    @ExcelExport(value = "功能域id",example = "1")
    private Integer functionId;

    /**
     * 场景id
     */
    @Schema(description = "场景id")
    @NotNull(message = "场景id不能为空")
    @ExcelImport(value = "场景id",required = true)
    @ExcelExport(value = "场景id",example = "1")
    private Integer scenarioId;

    /**
     * 场景任务
     */
    @Schema(description = "场景任务")
    @ExcelImport(value = "场景任务")
    @ExcelExport(value = "场景任务",example = "模板示例")
    private String scenarioTask;

    /**
     * 任务细分
     */
    @Schema(description = "任务细分")
    @ExcelImport(value = "任务细分")
    @ExcelExport(value = "任务细分",example = "模板示例")
    private String taskDetail;

    /**
     * 计分方式
     */
    @Schema(description = "计分方式")
    @ExcelImport(value = "计分方式")
    @ExcelExport(value = "计分方式",example = "模板示例")
    private String scoringMethod;

    /**
     * 评分标准
     */
    @Schema(description = "评分标准")
    @ExcelImport(value = "评分标准")
    @ExcelExport(value = "评分标准",example = "模板示例")
    private String scoringCriteria;

    /**
     * 行数
     */
    private int rowNum;
    /**
     * 错误提示
     */
    private String rowTips;

}
