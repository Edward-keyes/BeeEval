package com.xailab.vehicle.operation.testplatform.pojo.response;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class FunctionTreeSyncTaskInfoEditRequest implements Serializable {

    /**
     * 关联同步任务流水号
     */
    private String taskSerial;

    /**
     * 功能id
     */
    private String functionTag;

    /**
     * 同步选项数据
     * 通过-拼接
     */
    private List<String> syncOptionList;


}
