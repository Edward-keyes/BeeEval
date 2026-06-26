package com.xailab.vehicle.operation.testplatform.pojo.request;

import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTestCaseRateStateEnum;
import lombok.Data;

/**
 * @ClassName: TestCaseStateEditRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/5 17:01
 */
@Data
public class TestCaseStateEditRequest {
    private Integer testStateId;
    /**
     * 测试用例素材状态
     * na 缺数据
     * poor 没有素材
     * good 正常上传
     * show 首页展示
     *
     * @see FunctionTreeTestCaseRateStateEnum
     */
    private String materialState;

}
