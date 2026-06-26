package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeFirstTypeVO;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeFirstTypeQuery;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeFirstTypeEntity;

import java.util.List;

/**
 * 功能树一级标签类型
 *
 * @author mu 
 * @since  2025-05-31
 */
public interface FunctionTreeFirstTypeService extends BaseService<FunctionTreeFirstTypeEntity> {

    PageResult<FunctionTreeFirstTypeVO> page(FunctionTreeFirstTypeQuery query);

    void save(FunctionTreeFirstTypeVO vo);

    void update(FunctionTreeFirstTypeVO vo);

    void delete(List<Long> idList);
}