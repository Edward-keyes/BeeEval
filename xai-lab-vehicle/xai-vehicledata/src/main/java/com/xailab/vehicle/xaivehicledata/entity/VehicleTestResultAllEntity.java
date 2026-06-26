package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("vehicle_test_result_all")
public class VehicleTestResultAllEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 用例
     */
    private String example;

    /**
     * 用例
     */
    private String exampleEn;

    /**
     * 功能域id
     */
    private Long domainFunctionId;

    /**
     * 三级指标id
     */
    private Long functionIndexId;

    /**
     * 得分
     */
    private Integer score;
}