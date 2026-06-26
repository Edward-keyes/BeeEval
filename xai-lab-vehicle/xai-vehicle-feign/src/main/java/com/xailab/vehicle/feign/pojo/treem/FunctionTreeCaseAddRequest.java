package com.xailab.vehicle.feign.pojo.treem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class FunctionTreeCaseAddRequest implements Serializable {

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
     * 测试用例内容
     */
    private String caseContent;


    /**
     * 测试用例内容英文
     */
    private String caseContentEn;

    /**
     * 测试用例选项
     */
    private String caseOptions;

    /**
     * 是否首页显示
     */
    private Boolean isShow;


    /**
     * 测试结果
     * 功能评价 0: na/1:modest/2:avg/3:good
     */
    private Integer functionEvaluation;

}
