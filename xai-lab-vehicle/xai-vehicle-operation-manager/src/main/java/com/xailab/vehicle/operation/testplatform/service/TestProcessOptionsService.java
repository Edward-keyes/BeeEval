package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessOptionsVO;
import com.xailab.vehicle.operation.testplatform.query.TestProcessOptionsQuery;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessOptionsEntity;

import java.util.List;

/**
 * 流程选项表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface TestProcessOptionsService extends BaseService<TestProcessOptionsEntity> {

    PageResult<TestProcessOptionsVO> page(TestProcessOptionsQuery query);

    void save(TestProcessOptionsVO vo);

    void update(TestProcessOptionsVO vo);

    void delete(List<Long> idList);
}