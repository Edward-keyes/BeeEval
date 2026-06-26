package com.xailab.vehicle.xaivehicledata.entity.response;

import com.xailab.vehicle.xaivehicledata.entity.vo.ThreeFunctionGradeVo;
import lombok.Data;

import java.util.List;

@Data
public class VehicleFunctionGradeResponse {

    private String oneTagName;

    private String oneTagId;

    private List<VehicleFunctionTwoGradeResponse> vehicleFunctionTwoGradeResponses;

    @Data
    public static class VehicleFunctionTwoGradeResponse {

        private String twoTagName;

        private String twoTagId;

        private List<ThreeFunctionGradeVo> threeFunctionGradeVos;

    }

}