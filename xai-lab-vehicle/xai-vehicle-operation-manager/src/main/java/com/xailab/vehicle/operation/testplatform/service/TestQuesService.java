package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesVO;
import com.xailab.vehicle.operation.testplatform.query.TestQuesQuery;
import com.xailab.vehicle.operation.testplatform.entity.TestQuesEntity;

import java.util.List;

/**
 * 功能评价数据表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface TestQuesService extends BaseService<TestQuesEntity> {

    PageResult<TestQuesVO> page(TestQuesQuery query);

    void save(TestQuesVO vo);

    void update(TestQuesVO vo);

    void delete(List<Long> idList);
}