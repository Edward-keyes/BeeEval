package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test_record_value")
public class TestRecordValueEntity {

    /**
     * `id` int(11) NOT NULL,
     *   `base_function` varchar(255) DEFAULT NULL COMMENT '基础能力',
     *   `record_id` int(11) DEFAULT NULL COMMENT '任务id',
     *   `value` double DEFAULT NULL COMMENT '值',
     */

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String baseFunction;

    private Integer recordId;

    private Double value;

}
