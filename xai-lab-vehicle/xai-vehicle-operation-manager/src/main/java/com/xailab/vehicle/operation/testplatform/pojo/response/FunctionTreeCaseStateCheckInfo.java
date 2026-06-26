package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.xailab.vehicle.operation.testplatform.vo.TestProcessOptionsStateVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: FunctionTreeCaseStateCheckInfo
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 22:34
 */
@Data
public class FunctionTreeCaseStateCheckInfo {

    /**
     * state_id
     * 数据id
     */
    private Integer id;

    /**
     * process id
     */
    private Integer processId;

    /**
     * 测试状态id
     */
    private Integer stateId;

    /**
     * 题目类型
     * 0 : 问题
     * 1 : 选项
     */
    private String optionsType;


    /**
     * 步骤顺序
     */
    private Integer step;

    /**
     * 流程题目
     */
    private String processTitle;

    /**
     * 流程描述
     */
    private String description;

    /**
     * 提示
     */
    private String tip;

    /**
     * 题目类型 为：0:选项 展示
     * 问题1
     */
    private String quesOne;

    /**
     * 题目类型 为：0:选项 展示
     * 问题2
     */
    private String quesTwo;

    /**
     * 题目类型 为：0:选项 展示
     * 问题3
     */
    private String quesThree;


    @Schema(description = "是否成功 1成功 0失败")
    private Integer isSuccess;

    @Schema(description = "多选输入其他")
    private String other;

    @Schema(description = "标记问题 选项 1功能未搭载 2不具备测试条件 3用例未覆盖 4其他")
    private Integer errorSelect;

    @Schema(description = "标记问题 4其他输入框")
    private String error;


    /**
     * 题目类型 为：1:选项 展示
     * 选项
     */
    private List<TestProcessOptionsStateVO> options;

}
