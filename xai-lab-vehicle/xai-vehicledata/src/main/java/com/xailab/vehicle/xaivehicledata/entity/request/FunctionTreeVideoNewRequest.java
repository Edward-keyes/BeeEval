package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class FunctionTreeVideoNewRequest {

    /**
     * 三级标签描述
     */
    private String description;

    /**
     * 用例数据
     */
    private List<FunctionCaseData> caseDataList;

}
