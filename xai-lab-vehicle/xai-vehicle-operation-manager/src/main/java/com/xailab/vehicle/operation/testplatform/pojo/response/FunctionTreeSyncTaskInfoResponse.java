package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: FunctionTreeSyncTaskInfoResponse
 * @Description:
 * @author: liulin
 * @date: 2025/7/16 22:57
 */
@Data
public class FunctionTreeSyncTaskInfoResponse implements Serializable {


    /**
     * 关联同步任务流水号
     */
    private String taskSerial;


    /**
     * 功能id
     */
    private String functionTag;

    /**
     * 当且同步任务规则 为自定义关联 时需指定
     * 目标功能id
     */
    private String targetFunctionTag;


    /**
     * 使用List进行拼接 逗号分隔
     * id1,id2,id3
     * 测试用例ids
     */
    private String testCaseId;

    /**
     * 同步选项数据
     * 通过-拼接
     */
    private List<String> syncOptionList;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN, timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN, timezone = "GMT+8")
    private Date updateTime;


}
