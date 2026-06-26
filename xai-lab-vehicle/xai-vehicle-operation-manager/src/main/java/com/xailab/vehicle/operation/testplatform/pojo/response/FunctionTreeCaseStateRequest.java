package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

/**
 * @ClassName: FunctionTreeCaseStateRequest
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 23:11
 */
@Data
public class FunctionTreeCaseStateRequest {

    /**
     * 测试任务id
     */
    private Integer testRecordId;

    /**
     * 测试用例id
     */
    private Integer testCaseId;


}
