package com.xailab.vehicle.operation.testplatform.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeSyncSelectResponse
 * @Description: 功能树功能树已选择的id
 * @author: liulin
 * @date: 2025/6/9 22:46
 */
@Data
public class FunctionTreeSyncSelectResponse implements Serializable {
    /**
     * 选中的功能标签
     */
    private List<String> functionTagList;

    /**
     * 选中的测试用例id
     */
    private List<Integer> testCaseIdList;

    /**
     * 选中的beeeval编号
     */
    private List<String> selectBeeevalNumberList;

    /**
     * 选中beeeval的测试用例id
     */
    private List<Integer> selectBeeevalCaseList;

}
