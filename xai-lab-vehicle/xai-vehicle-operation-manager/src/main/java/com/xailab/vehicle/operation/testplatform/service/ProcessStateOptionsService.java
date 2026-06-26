package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.ProcessStateOptionsVO;
import com.xailab.vehicle.operation.testplatform.query.ProcessStateOptionsQuery;
import com.xailab.vehicle.operation.testplatform.entity.ProcessStateOptionsEntity;

import java.util.List;

/**
 * 流程多选项 选择结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface ProcessStateOptionsService extends BaseService<ProcessStateOptionsEntity> {

    PageResult<ProcessStateOptionsVO> page(ProcessStateOptionsQuery query);

    void save(ProcessStateOptionsVO vo);

    void update(ProcessStateOptionsVO vo);

    void delete(List<Long> idList);
}