package com.xailab.vehicle.operation.testplatform.pojo.request;

import com.xailab.vehicle.operation.testplatform.vo.EditBasicFunctionVo;
import lombok.Data;

import java.util.List;

@Data
public class EditBasicFunctionRequest {

    private Integer vehicleId;

    private List<EditBasicFunctionVo> basicFunctionList;

}