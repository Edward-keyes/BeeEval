package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateVO;
import com.xailab.vehicle.operation.testplatform.query.QuesStateQuery;
import com.xailab.vehicle.operation.testplatform.entity.QuesStateEntity;

import java.util.List;

/**
 * 功能评价结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface QuesStateService extends BaseService<QuesStateEntity> {

    PageResult<QuesStateVO> page(QuesStateQuery query);

    void save(QuesStateVO vo);

    void update(QuesStateVO vo);

    void delete(List<Long> idList);
}