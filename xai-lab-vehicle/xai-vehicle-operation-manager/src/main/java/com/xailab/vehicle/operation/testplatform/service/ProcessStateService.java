package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.ProcessStateVO;
import com.xailab.vehicle.operation.testplatform.query.ProcessStateQuery;
import com.xailab.vehicle.operation.testplatform.entity.ProcessStateEntity;

import java.util.List;

/**
 * 流程状态结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface ProcessStateService extends BaseService<ProcessStateEntity> {

    PageResult<ProcessStateVO> page(ProcessStateQuery query);

    void save(ProcessStateVO vo);

    void update(ProcessStateVO vo);

    void delete(List<Long> idList);
}