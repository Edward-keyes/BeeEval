package com.xailab.vehicle.operation.testplatform.pojo.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeSyncTaskInfoResponse
 * @Description:
 * @author: liulin
 * @date: 2025/7/16 22:57
 */
@Data
public class FunctionTreeSyncTaskCaseOptionEditRequest implements Serializable {

    /**
     * 关联同步任务流水号
     */
    @NotBlank(message = "关联同步任务流水号不能为空")
    private String taskSerial;

    /**
     * 功能id
     */
    @NotBlank(message = "功能id不能为空")
    private String functionTag;


    /**
     * 同步选项数据
     */
    @NotEmpty(message = "同步选项数据不能为空")
    private List<TestCaseSyncOptionInfo> testCaseSyncOptions;

    @Data
    public static class TestCaseSyncOptionInfo implements Serializable {
        /**
         * 测试用例id
         */
        private Integer testCaseId;

        /**
         * 同步选项数据
         * 通过-拼接
         */
        private List<String> syncOptionList;

    }


}
