package com.xailab.vehicle.feign.pojo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeDataSyncRequest
 * @Description:
 * @author: liulin
 * @date: 2025/6/12 23:51
 */
@Data
public class FunctionTreeDataSyncRequest implements Serializable {

    /**
     * 任务序列号
     */
    private String taskSerial;

    /**
     * 测试任务id
     * 原始车辆
     */
    private String testRecordId;

    /**
     * 车辆id
     * 目标车辆
     * beeeval 平台对应车辆id
     */
    private String vehicleId;

    /**
     * 同步数据详情
     */
    private List<FunctionTreeDataSyncInfoRequest> syncInfo;


}
