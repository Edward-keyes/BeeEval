package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FunctionTreeSyncTaskCaseOptionResponse implements Serializable {

    /**
     * 测试用例id
     */
    private Integer testCaseId;

    /**
     * 同步选项
     */
    private List<String> syncOptionList;


}
