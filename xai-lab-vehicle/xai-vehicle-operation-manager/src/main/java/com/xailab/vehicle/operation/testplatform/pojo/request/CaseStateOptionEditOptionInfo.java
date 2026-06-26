package com.xailab.vehicle.operation.testplatform.pojo.request;

import lombok.Data;

/**
 * @ClassName: CaseStateOptionAddOptionInfo
 * @Description:
 * @author: liulin
 * @date: 2025/5/5 15:01
 */
@Data
public class CaseStateOptionEditOptionInfo {

    /**
     * 选项stateId 为空为新增，不为空为编辑
     */
    private Integer id;
    /**
     * 选择选项的id
     */
    private Integer selectId;
    /**
     * 选项状态
     */
    private String dataState;
}
