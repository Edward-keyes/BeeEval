package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("vehicle_domain_test_case")
public class DomainTestCaseEntity {

    @TableId
    private Long id;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 功能域id
     */
    private Long domainIndexId;

    /**
     * 分值
     */
    private Integer score;

    /**
     * 测试指令
     */
    private String testInstruct;

    /**
     * 响应截图图片路径
     */
    private String responsePhotoName;

    /**
     * 结果分析
     */
    private String interpretationOfResult;

}
