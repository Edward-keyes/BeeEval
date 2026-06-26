package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 功能域映射关系表
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Data
@TableName("pcafe_relevancy_function_domain_mapping")
public class PcafeFunctionDomainMappingEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 数据管理平台功能域名称
     */
    private String testFunctionDomainName;

    /**
     * BeeEval功能域ID
     */
    private Long beeevalFunctionDomainId;

    /**
     * BeeEval功能域名称
     */
    private String beeevalFunctionDomainName;

}
