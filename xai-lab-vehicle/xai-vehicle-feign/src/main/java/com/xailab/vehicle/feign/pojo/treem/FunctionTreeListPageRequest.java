package com.xailab.vehicle.feign.pojo.treem;

import com.xailab.vehicle.feign.common.PageQuery;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FunctionTreeListPageRequest extends PageQuery {

    /**
     * 车辆编号
     */
    @NotNull(message = "车辆信息不能为空")
    private Long vehicleId;

    /**
     * 标签编号
     */
    private String tagNumber;

    /**
     * 标签名称
     */
    private String tagName;
}
