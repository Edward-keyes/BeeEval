package com.xailab.vehicle.operation.testplatform.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.xailab.vehicle.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
* 测试用例总表查询
*
* @author mumu 
* @since 1.0.0 2025-04-16
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "测试用例总表查询")
public class TestPlatformVehicleTestCaseQuery extends Query {
    @Schema(description = "用例内容")
    private String testcaseContent;

    @Schema(description = "功能域id")
    private Long functionId;

    /**
     * 对比的车辆ids
     */
    private List<Integer> compareVehicleIds;

    /**
     * 筛选方差操作条件
     * ge 大于等于
     * le 小于等于
     * gt 大于
     * lt 小于
     * eq 等于
     */
    private String varianceOperator;

    /**
     * 方差值
     */
    private Double varianceValue;

}