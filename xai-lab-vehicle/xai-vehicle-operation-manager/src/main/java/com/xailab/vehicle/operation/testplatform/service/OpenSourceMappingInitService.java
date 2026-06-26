package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.feign.common.Result;

/**
 * 开源用例关联映射初始化服务
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
public interface OpenSourceMappingInitService {

    /**
     * 初始化测试用例与开源用例的关联关系
     * 根据testcase_content字段匹配
     *
     * @return 初始化结果
     */
    Result initOpenSourceMapping();
}
