package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: QuesStateOptionInfoVO
 * @Description:
 * @author: liulin
 * @date: 2025/5/4 0:45
 */
@Data
public class QuesStateOptionInfoVO {

    /**
     * ques_option_state_id
     * 选择选项数据id
     */
    private Integer id;

    /**
     * 问题数据id
     */
    private Integer quesStateId;

    /**
     * ques_option
     * 选项id
     */
    private Integer selectId;

    @Schema(description = "选项")
    private String options;

    @Schema(description = "排序")
    private Integer sort;

    /**
     * 数据状态
     * na ：预设
     * good：Good
     * poor：Poor
     */
    @Schema(description = "数据状态")
    private String dataState;
}
