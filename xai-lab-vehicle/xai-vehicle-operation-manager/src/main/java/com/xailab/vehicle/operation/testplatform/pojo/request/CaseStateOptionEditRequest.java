package com.xailab.vehicle.operation.testplatform.pojo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: CaseStateOptionAddRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/5 13:12
 */
@Data
public class CaseStateOptionEditRequest {
    /**
     * state_Id
     * 题目状态id
     */
    @NotNull(message = "题目状态id不能为空")
    private Integer id;

    /**
     * 测试用例类型
     * 0：功能评价
     * 1：功能走查
     */
    @NotNull(message = "测试用例类型不能为空")
    private Integer caseType;


    /**
     * 其他选项保存
     */
    private String other;


    /**
     * 选择错误选项id
     */
    private Integer errorSelect;

    /**
     * 其他错误描述
     */
    private String error;


    /**
     * 选项详情
     * 测试用例类型 为功能评价和功能走查 可指定
     * 但功能走查，功能走查中的题目类型必须为1
     */
//    @NotEmpty(message = "选项详情不能为空")
    private List<CaseStateOptionEditOptionInfo> options;


}
