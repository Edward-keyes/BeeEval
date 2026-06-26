package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.xailab.vehicle.operation.testplatform.vo.QuesStateOptionInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: FunctionTreeCaseStateEvaluateInfo
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 21:55
 */
@Data
public class FunctionTreeCaseStateEvaluateInfo {

    /**
     * 功能评价 用户测试id
     */
    private Integer id;


    /**
     * 测试状态id (test_state)
     */
    private Integer stateId;

    /**
     * 问题结构id
     * test_ques id
     */
    private Integer quesId;

    /**
     * 其他
     */
    private String other;

    /**
     * 选项类型 0单选 1多选
     */
    private Integer optionsType;

    /**
     * 类型
     */
    private String categoryType;

    /**
     * 问题/维度
     */
    private String questionDimension;


    /**
     * 选项
     */
    private List<QuesStateOptionInfoVO> options;


}
