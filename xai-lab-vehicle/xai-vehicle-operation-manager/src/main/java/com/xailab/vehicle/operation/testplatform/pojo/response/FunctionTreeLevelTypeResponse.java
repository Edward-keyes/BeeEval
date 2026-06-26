package com.xailab.vehicle.operation.testplatform.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: FunctionTreeLevelTypeResponse
 * @Description:
 * @author: liulin
 * @date: 2025/5/31 1:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionTreeLevelTypeResponse implements Serializable {
    @Schema(description = "标签")
    private String label;

    @Schema(description = "标签名称")
    private String labelName;
}
