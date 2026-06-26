package com.xailab.vehicle.operation.testplatform.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取测试场景详情
 * @ClassName: TestPlatformTestScenarioResVo
 * @Description:
 * @author: liulin
 * @date: 2025/4/18 23:03
 */
@Data
public class TestPlatformTestScenarioResVo implements Serializable {

    /**
     * 场景id
     */
    private Integer id;

    /**
     * 场景名
     */
    private String scenarioName;

}
