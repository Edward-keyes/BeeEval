package com.xailab.vehicle.xaivehicledata.entity.response;

import com.xailab.vehicle.xaivehicledata.entity.vo.Problem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
public class TestResultResponse {

        private String vehicleName;

        private String vehicleId;

        private List<Problem> problemList;
}
