package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.xailab.vehicle.operation.testplatform.vo.TestProcessOptionsVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: FunctionTreeCaseProcessInfo
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 14:36
 */
@Data
public class FunctionTreeCaseProcessInfo {

    /**
     * process id
     */
    private Integer id;


    @Schema(description = "步骤顺序")
    private Integer step;

    /**
     * 题目类型
     * 0 : 问题
     * 1 : 选项
     */
    private String optionsType;
    /**
     * 流程题目 不可编辑
     */
    @Schema(description = "流程题目")
    private String processTitle;

    @Schema(description = "流程描述")
    private String description;

    @Schema(description = "提示")
    private String tip;


    /**
     * 题目类型 为：0:问题 展示
     * 问题1
      */
    @Schema(description = "问题1")
    private String quesOne;

    /**
     * 题目类型 为：0:问题 展示
     * 问题2
     */
    @Schema(description = "问题2")
    private String quesTwo;

    /**
     * 题目类型 为：0:问题 展示
     * 问题3
     */
    @Schema(description = "问题3")
    private String quesThree;


    /**
     * 题目类型 为：1:选项 展示
     * 选项标题
     */
    @Schema(description = "选项标题")
    private String optionsTitle;

    /**
     * 题目类型 为：1:选项 展示
     * 选项
     */
    private List<TestProcessOptionsVO> options;


}
