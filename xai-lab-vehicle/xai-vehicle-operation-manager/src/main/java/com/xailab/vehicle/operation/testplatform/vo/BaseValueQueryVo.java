package com.xailab.vehicle.operation.testplatform.vo;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

@Data
public class BaseValueQueryVo {

    private Integer totalQuestions;

    private Integer scoreCount;

    private Double scorePercentage;

}
