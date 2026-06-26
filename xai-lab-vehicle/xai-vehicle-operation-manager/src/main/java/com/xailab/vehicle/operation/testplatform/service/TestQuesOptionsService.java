package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesOptionsVO;
import com.xailab.vehicle.operation.testplatform.query.TestQuesOptionsQuery;
import com.xailab.vehicle.operation.testplatform.entity.TestQuesOptionsEntity;

import java.util.List;

/**
 * 功能评价选项表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface TestQuesOptionsService extends BaseService<TestQuesOptionsEntity> {

    PageResult<TestQuesOptionsVO> page(TestQuesOptionsQuery query);

    void save(TestQuesOptionsVO vo);

    void update(TestQuesOptionsVO vo);

    void delete(List<Long> idList);
}