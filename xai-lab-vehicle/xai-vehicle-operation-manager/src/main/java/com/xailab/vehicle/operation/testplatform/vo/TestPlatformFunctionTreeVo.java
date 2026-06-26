package com.xailab.vehicle.operation.testplatform.vo;

import lombok.Data;

/**
 * @ClassName: TestPlatformFunctionTreeVo
 * @Description:
 * @author: liulin
 * @date: 2025/4/28 23:17
 */
@Data
public class TestPlatformFunctionTreeVo {


    private String taskDetail;

    /**
     * 场景任务
     * 二级标签
     */
    private String scenarioTask;

    /**
     * 域id
     */
    private Integer functionId;

    /**
     * 域名称
     */
    private String functionDomainName;


    /**
     * 分页查询出参数
     */
    /**
     * 功能评价
     */
    private String functionEvaluate;

    /**
     * 数据状态
     */
    private Integer dataState;

}
