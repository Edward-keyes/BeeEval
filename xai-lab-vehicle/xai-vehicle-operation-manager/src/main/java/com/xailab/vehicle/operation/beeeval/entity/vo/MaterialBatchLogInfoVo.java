package com.xailab.vehicle.operation.beeeval.entity.vo;

import com.xailab.vehicle.operation.beeeval.entity.MaterialBatchLogInfoEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MaterialBatchLogInfoVo {

    /**
     * 批次号
     */
    private Integer batchNum;

    /**
     * 批上传任务名
     */
    private String batchName;

    /**
     * 素材分类(1:功能树,2:测评)
     */
    private Integer materialClassify;

    /**
     * 执行时间
     */
    private Date executeDate;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 状态(0:等待同步,1:同步中,2:同步完成,-1:关闭同步)
     */
    private Integer status;

    /**
     * 异步同步素材数据详情
     */
    private List<MaterialBatchLogInfoEntity> materialBatchLogInfoEntityList;
}
