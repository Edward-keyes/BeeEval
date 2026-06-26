package com.xailab.vehicle.operation.testplatform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: FunctionTreeSyncTaskInfoListVO
 * @Description:
 * @author: liulin
 * @date: 2025/6/5 0:19
 */
@Data
public class FunctionTreeSyncTaskInfoListVO implements Serializable {
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "关联同步任务流水号")
    private String taskSerial;

    @Schema(description = "功能id")
    private String functionTag;

    @Schema(description = "目标功能id（当同步任务规则为自定义关联时需指定）")
    private String targetFunctionTag;

    @Schema(description = "测试用例id")
    private List<Integer> testCaseId;

    /**
     * 同步选项数据
     * 通过-拼接
     */
    private String syncOption;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date updateTime;


    /**
     * 设置是否删除
     * @param testCaseId
     */
    public void setTestCaseId(String testCaseId){
        if (StringUtils.isBlank(testCaseId)){
            return;
        }
        this.testCaseId = Arrays.stream(StringUtils.split(testCaseId, ",")).map(Integer::parseInt).toList();
    }
}
