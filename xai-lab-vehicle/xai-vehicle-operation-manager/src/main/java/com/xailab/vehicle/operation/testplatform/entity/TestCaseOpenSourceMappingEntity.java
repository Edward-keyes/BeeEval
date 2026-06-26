package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 测试用例与开源用例关联表
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Data
@TableName("test_case_open_source_mapping")
public class TestCaseOpenSourceMappingEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 数据管理平台测试用例ID
     */
    private Integer testCaseId;

    /**
     * BeeEval开源用例ID
     */
    private Integer beeevalOpenCaseId;
}
