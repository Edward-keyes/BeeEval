package com.xailab.vehicle.operation.testplatform.pojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: TestPlatformTestCaseImportByExcelError
 * @Description:
 * @author: liulin
 * @date: 2025/4/22 22:52
 */
@Data
@NoArgsConstructor
public class TestPlatformImportByExcelResultResponse implements Serializable {


    /**
     * 成功数量
     */
    @Schema(description = "成功数量")
    private Integer successNumber;

    /**
     * 失败数量
     */
    @Schema(description = "失败数量")
    private Integer failNumber;

    /**
     * 失败详情
     */
    @Schema(description = "失败详情")
    private List<FailNumberInfo> failInfo;


    /**
     * 失败详情
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class FailNumberInfo{
        /**
         * 数据下标
         */
        @Schema(description = "数据下标")
        private Integer indexNumber;

        /**
         * 序号
         */
        @Schema(description = "数据序号")
        private Integer dataSerial;

        /**
         * 错误信息
         */
        @Schema(description = "错误信息")
        private String message;
    }



}
