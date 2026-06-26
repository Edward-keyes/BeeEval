package com.xailab.vehicle.feign.vo;

import com.xailab.vehicle.feign.vo.OpenSourceVo;
import lombok.Data;

import java.util.List;

@Data
public class VehicleIdAndOpenSourceVo {

    private String vehicleId;

    private List<OpenSourceVo> openSourceVos;

}
