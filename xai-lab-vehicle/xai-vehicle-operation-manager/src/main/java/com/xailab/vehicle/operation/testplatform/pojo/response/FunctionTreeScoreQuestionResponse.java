package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesOptionsVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: FunctionTreeScoreFrameworkResponse
 * @Description:
 * @author: liulin
 * @date: 2025/5/2 16:42
 */
@Data
public class FunctionTreeScoreQuestionResponse implements Serializable {

    @Schema(description = "问题id")
    private Integer id;

//    @Schema(description = "功能树分类")
//    private String functionType;

    @Schema(description = "三级名称")
    private String testName;

    @Schema(description = "选项类型 0单选 1多选")
    private Integer optionsType;

    @Schema(description = "类型")
    private String categoryType;

    @Schema(description = "问题/维度")
    private String questionDimension;

    @Schema(description = "状态 0正常 1 停用")
    private Integer state;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 选项
     */
    @Schema(description = "选项")
    private List<TestQuesOptionsVO> options;

}
