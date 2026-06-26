package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysSmsLogEntity;
import com.xailab.vehicle.operation.system.query.SysSmsLogQuery;
import com.xailab.vehicle.operation.system.vo.SysSmsLogVO;

import java.util.List;

/**
 * 短信日志
 *

 */
public interface SysSmsLogService extends BaseService<SysSmsLogEntity> {

    PageResult<SysSmsLogVO> page(SysSmsLogQuery query);

    void delete(List<Long> idList);
}