package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

/**
 * @ClassName: TestCaseOptionsInfoResponse
 * @Description:
 * @author: liulin
 * @date: 2025/5/5 15:51
 */
@Data
public class TestCaseOptionsInfoResponse {

    /**
     * 选项id
     */
    private Integer id;

    /**
     * 选项内容
     */
    private String option;

    /**
     * 排序字段
     */
    private Integer sort;

}
