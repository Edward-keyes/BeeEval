package com.xailab.vehicle.xaivehicledata.entity.vo;

import com.xailab.vehicle.xaivehicledata.entity.response.VehicleFunctionGradeVoResponse;
import lombok.Data;

import java.util.List;

@Data
public class ThreeFunctionGradeVo {

    private String threeTagName;

    private String threeTagId;

    private List<VehicleFunctionGradeVoResponse> vehiclefunctionGrade;

}
