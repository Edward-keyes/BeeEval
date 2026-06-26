package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.ErrorTypeVO;
import com.xailab.vehicle.operation.testplatform.query.ErrorTypeQuery;
import com.xailab.vehicle.operation.testplatform.entity.ErrorTypeEntity;

import java.util.List;

/**
 * 
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface ErrorTypeService extends BaseService<ErrorTypeEntity> {

    PageResult<ErrorTypeVO> page(ErrorTypeQuery query);

    void save(ErrorTypeVO vo);

    void update(ErrorTypeVO vo);

    void delete(List<Long> idList);
}