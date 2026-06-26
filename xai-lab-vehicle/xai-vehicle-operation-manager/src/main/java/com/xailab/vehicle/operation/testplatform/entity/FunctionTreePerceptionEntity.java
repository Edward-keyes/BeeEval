package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("function_tree_perception")
public class FunctionTreePerceptionEntity {

    /**
     * CREATE TABLE `function_tree_perception` (
     *   `id` int(11) NOT NULL AUTO_INCREMENT,
     *   `record_id` int(11) DEFAULT NULL COMMENT '任务id',
     *   `perception_id` int(11) DEFAULT NULL COMMENT '感知任务id',
     *   `is_have` smallint(6) DEFAULT NULL COMMENT '0:无,1:未知,2:有',
     *   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     *   PRIMARY KEY (`id`)
     * )
     */

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer recordId;

    private Integer perceptionId;

    private Integer isHave;

    private Date createTime;

}
