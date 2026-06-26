package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("pcafe_relevancy_function_domain")
public class PcafeRelevancyFunctionDomainEntity {

    /**
     * `id` int(11) NOT NULL,
     *   `test_function_domain_name` varchar(255) DEFAULT NULL COMMENT '测试软件功能域名称',
     *   `test_index_name` int(11) DEFAULT NULL COMMENT '指标名称',
     *   `beeeval_index_id` bigint(20) DEFAULT NULL COMMENT 'beeeval指标id',
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String testFunctionDomainName;

    private String testIndexName;

    private Long beeevalIndexId;

}
