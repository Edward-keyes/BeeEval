package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysMailLogEntity;
import com.xailab.vehicle.operation.system.query.SysMailLogQuery;
import com.xailab.vehicle.operation.system.vo.SysMailLogVO;

import java.util.List;

/**
 * 邮件日志
 *
 * 
 */
public interface SysMailLogService extends BaseService<SysMailLogEntity> {

    PageResult<SysMailLogVO> page(SysMailLogQuery query);

    void delete(List<Long> idList);
}