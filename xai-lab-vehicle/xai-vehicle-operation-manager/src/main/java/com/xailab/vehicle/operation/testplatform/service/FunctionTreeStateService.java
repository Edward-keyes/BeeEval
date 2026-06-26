package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeStateVO;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeStateQuery;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeStateEntity;

import java.util.List;

/**
 * 功能树状态表
 *
 * @author mumu 
 * @since 1.0.0 2025-05-18
 */
public interface FunctionTreeStateService extends BaseService<FunctionTreeStateEntity> {

    PageResult<FunctionTreeStateVO> page(FunctionTreeStateQuery query);

    void save(FunctionTreeStateVO vo);

    void update(FunctionTreeStateVO vo);

    void delete(List<Long> idList);
}