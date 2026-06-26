package com.xailab.vehicle.feign.pojo.treem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class FunctionTreeCaseMaterialQueryRequest implements Serializable {
    /**
     * 车辆编号
     */
    @NotNull(message = "车辆信息不能为空")
    private Long vehicleId;

    /**
     * 用例id
     */
    @NotNull(message = "用例信息不能为空")
    private Long functionTreeCaseId;
}
