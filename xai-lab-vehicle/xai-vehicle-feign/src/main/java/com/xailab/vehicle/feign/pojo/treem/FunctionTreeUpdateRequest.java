package com.xailab.vehicle.feign.pojo.treem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class FunctionTreeUpdateRequest implements Serializable {

    /**
     * 车辆编号
     */
    @NotNull(message = "车辆信息不能为空")
    private Long vehicleId;


    /**
     * 三级标签编号
     */
    @NotBlank(message = "三级标签编号不能为空")
    private String tagNumber;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    private String tagName;

    /**
     * 标签名称英文
     */
    private String tagNameEn;

    /**
     * 说明
     */
    private String description;

    /**
     * 说明英文
     */
    private String descriptionEn;

    /**
     * 功能评价
     * 功能清单(1:效果最佳,2:有,3:不及预期,4:无)
     */
    @NotNull(message = "功能清单不能为空")
    private Integer functionList;


}
