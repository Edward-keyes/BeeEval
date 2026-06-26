package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 200道开源用例表
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Data
@TableName("beeeval_open_source_case")
public class BeeevalOpenSourceCaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 功能域ID
     */
    private Long functionDomainId;

    /**
     * 功能域指标ID
     */
    private Long domainIndexId;

    /**
     * 测试用例内容
     */
    private String testCaseContent;

    /**
     * 测试用例内容英文
     */
    private String testCaseContentEn;
}
