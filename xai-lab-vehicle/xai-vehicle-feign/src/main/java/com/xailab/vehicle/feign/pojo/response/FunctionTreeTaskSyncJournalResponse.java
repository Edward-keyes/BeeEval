package com.xailab.vehicle.feign.pojo.response;

import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncInfoRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: FunctionTreeTaskSyncJournalResponse
 * @Description: 功能树数据同步记录
 * @author: liulin
 * @date: 2025/7/6 16:45
 */
@Data
public class FunctionTreeTaskSyncJournalResponse implements Serializable {

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
    private List<FunctionTreeDataSyncInfoJournal> syncInfo;


}
