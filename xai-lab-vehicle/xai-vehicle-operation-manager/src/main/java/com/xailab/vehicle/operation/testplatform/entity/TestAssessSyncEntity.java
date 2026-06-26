package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test_assess_sync")
public class TestAssessSyncEntity {

    /**
     * CREATE TABLE `test_assess_sync` (
     *   `id` int(11) NOT NULL AUTO_INCREMENT,
     *   `beeeval_function_damain_id` bigint(20) DEFAULT NULL COMMENT 'beeeval 功能域id',
     *   `pcafe_function_name` varchar(255) DEFAULT NULL COMMENT 'pcafe 功能域名称',
     *   PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
     */

    private Integer id;

    private Long beeevalFunctionDomainId;

    private String pcafeFunctionName;

}