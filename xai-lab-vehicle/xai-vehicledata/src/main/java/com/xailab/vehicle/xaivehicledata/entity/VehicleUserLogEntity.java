package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("vehicle_user_log")
public class VehicleUserLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 日志类型
     */
    private Integer logType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 操作类型
     */
    private Integer operationType;

    /**
     * 操作
     */
    private String operation;

    /**
     * 创建时间
     */
    private Date createDate;
}