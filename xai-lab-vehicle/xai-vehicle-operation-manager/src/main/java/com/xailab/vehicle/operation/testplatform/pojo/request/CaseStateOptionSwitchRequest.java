package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName: CaseStateOptionSwitchRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/5 11:19
 */
@Data
public class CaseStateOptionSwitchRequest {

    /**
     * 用户测试数据id
     */
    @NotNull(message = "stateId不能为空")
    private Integer id;

    /**
     * 测试用例类型
     * 0：功能评价
     * 1：功能走查
     */
    @NotNull(message = "测试用例类型不能为空")
    private Integer testCaseType;

}
