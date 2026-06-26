package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessVO;
import com.xailab.vehicle.operation.testplatform.query.TestProcessQuery;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessEntity;

import java.util.List;

/**
 * 流程数据表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface TestProcessService extends BaseService<TestProcessEntity> {

    PageResult<TestProcessVO> page(TestProcessQuery query);

    void save(TestProcessVO vo);

    void update(TestProcessVO vo);

    void delete(List<Long> idList);
}