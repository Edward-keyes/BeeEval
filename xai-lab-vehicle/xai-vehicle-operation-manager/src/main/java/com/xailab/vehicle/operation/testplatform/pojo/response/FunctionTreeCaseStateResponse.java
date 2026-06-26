package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTestCaseRateStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: FunctionTreeCaseStateResponse
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 21:49
 */
@Data
public class FunctionTreeCaseStateResponse {

    /**
     * 测试用例state id
     */
    private Integer id;

    /**
     * 测试状态 未测试：0；已测试：1
     */
    private Integer testStatus;

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

    /**
     * 测试用例类型
     * 0：功能评价
     * 1：功能走查
     */
    private Integer testCaseType;


    /**
     * 任务是否成功 初始：-1； 失败：0； 成功：1
     */
    private Integer isSuccessful;

    @Schema(description = "错误类型")
    private Integer errorType;

    @Schema(description = "错误详情")
    private String errorDetail;


    /**
     * 功能评价 数据
     */
    private List<FunctionTreeCaseStateEvaluateInfo> evaluateInfo;

    /**
     * 功能走查详情
     */
    private List<FunctionTreeCaseStateCheckInfo> checkInfo;


}
