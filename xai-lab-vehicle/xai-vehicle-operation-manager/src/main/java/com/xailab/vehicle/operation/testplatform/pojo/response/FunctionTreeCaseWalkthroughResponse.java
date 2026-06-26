package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.xailab.vehicle.operation.testplatform.vo.TestProcessOptionsVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: FunctionTreeCaseWalkthroughResponse
 * @Description:
 * @author: liulin
 * @date: 2025/5/24 1:57
 */
@Data
public class FunctionTreeCaseWalkthroughResponse {

    /**
     * id
     */
    private Integer id;

    /**
     * 功能域名称
     */
    private String functionDomainName;
    /**
     * 功能域id
     */
    private Integer functionId;

    /**
     * 三级
     */
    private String levelThreeName;

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
    @Schema(description = "提示")
    private String tip;

    /**
     * 功能走查备选问题详情
     */
    private WalkthroughAlternativeQuestions alternativeQuestions;

    /**
     * 选项设置
     */
    private WalkthroughOptionSettings optionSettings;



    /**
     * 功能走查备选问题详情
     */
    @Data
    public static class  WalkthroughAlternativeQuestions{

        /**
         * 问题1
         */
        private String quesOne;

        /**
         * 问题2
         */
        private String quesTwo;

        /**
         * 问题3
         */
        private String quesThree;

    }

    /**
     * 选项设置
     */
    @Data
    public static class WalkthroughOptionSettings{

        @Schema(description = "选项类型 0单选 1多选")
        private String optionsType;

        /**
         * 选项标题
         */
        private String optionsTitle;

        /**
         * 题目类型 为：1:选项 展示
         * 选项
         */
        private List<TestProcessOptionsVO> options;

    }

}
