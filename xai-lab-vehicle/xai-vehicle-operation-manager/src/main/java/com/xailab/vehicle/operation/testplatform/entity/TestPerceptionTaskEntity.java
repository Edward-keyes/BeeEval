package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("test_perception_task")
public class TestPerceptionTaskEntity {

    /**
     * CREATE TABLE `test_perception_task` (
     *   `id` int(11) NOT NULL AUTO_INCREMENT,
     *   `perception_name` varchar(50) DEFAULT NULL COMMENT '感知任务',
     *   `status` smallint(6) DEFAULT NULL COMMENT '0:禁用,1:使用',
     *   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     *   `type` smallint(6) DEFAULT NULL COMMENT '1:听觉感知,2:多模态感知',
     *   PRIMARY KEY (`id`)
     * )
     */

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String perceptionName;

    private Integer status;

    private Date createTime;

    private Integer type;
}
