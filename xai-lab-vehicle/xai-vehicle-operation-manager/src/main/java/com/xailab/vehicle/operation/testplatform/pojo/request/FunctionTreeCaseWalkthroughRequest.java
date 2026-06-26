package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName: findCaseWalkthroughInfo
 * @Description:
 * @author: liulin
 * @date: 2025/5/24 1:53
 */
@Data
public class FunctionTreeCaseWalkthroughRequest {

    /**
     * 二级
     */
    private String levelTwoName;

    /**
     * 三级
     */
    @NotBlank(message = "功能走查三级不能为空")
    private String levelThreeName;


}
