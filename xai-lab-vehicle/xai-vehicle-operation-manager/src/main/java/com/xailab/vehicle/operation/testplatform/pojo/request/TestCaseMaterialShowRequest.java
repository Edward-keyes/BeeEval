package com.xailab.vehicle.operation.testplatform.pojo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TestCaseMaterialShowRequest implements Serializable {

    /**
     * 测试记录id
     */
    private Integer recordId;
    /**
     * 测试用例id
     */
    private Integer testCaseId;
    /**
     * 素材ids
     */
    private List<Long> materialIds;

}
