package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateOptionsVO;
import com.xailab.vehicle.operation.testplatform.query.QuesStateOptionsQuery;
import com.xailab.vehicle.operation.testplatform.entity.QuesStateOptionsEntity;

import java.util.List;

/**
 * 问卷选项 选择结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
public interface QuesStateOptionsService extends BaseService<QuesStateOptionsEntity> {

    PageResult<QuesStateOptionsVO> page(QuesStateOptionsQuery query);

    void save(QuesStateOptionsVO vo);

    void update(QuesStateOptionsVO vo);

    void delete(List<Long> idList);
}