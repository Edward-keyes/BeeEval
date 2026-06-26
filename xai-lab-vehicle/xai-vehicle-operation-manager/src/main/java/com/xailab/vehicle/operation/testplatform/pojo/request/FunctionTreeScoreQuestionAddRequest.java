package com.xailab.vehicle.operation.testplatform.pojo.request;

import com.xailab.vehicle.operation.testplatform.vo.TestQuesOptionsVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeScoreFrameworkEditRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/2 16:42
 */
@Data
public class FunctionTreeScoreQuestionAddRequest implements Serializable {

    @Schema(description = "三级标签名称")
    @NotBlank(message = "三级标签不能为空")
    private String testName;

    @Schema(description = "选项类型 0单选 1多选")
    @NotNull(message = "选项类型不能为空")
    private Integer optionsType;

    @Schema(description = "类型")
    @NotBlank(message = "类型不能为空")
    private String categoryType;

    @Schema(description = "问题/维度")
    @NotBlank(message = "维度不能为空")
    private String questionDimension;

    @Schema(description = "状态 0正常 1 停用")
    private Integer state;

    /**
     * 选项
     */
    @Schema(description = "问题选项")
    @NotEmpty(message = "问题选项不能为空")
    private List<TestQuesOptionsVO> options;

}
