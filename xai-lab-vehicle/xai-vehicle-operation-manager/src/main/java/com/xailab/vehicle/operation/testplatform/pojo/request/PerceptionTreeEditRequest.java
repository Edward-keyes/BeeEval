package com.xailab.vehicle.operation.testplatform.pojo.request;

import com.xailab.vehicle.operation.testplatform.vo.PerceptionTreeEditVo;
import lombok.Data;

import java.util.List;

@Data
public class PerceptionTreeEditRequest {

    private Integer recordId;

    private List<PerceptionTreeEditVo> perceptionTreeEditVoList;
}
