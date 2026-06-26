package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysLogOperateEntity;
import com.xailab.vehicle.operation.system.query.SysLogOperateQuery;
import com.xailab.vehicle.operation.system.vo.SysLogOperateVO;

/**
 * 操作日志
 *

 */
public interface SysLogOperateService extends BaseService<SysLogOperateEntity> {

    PageResult<SysLogOperateVO> page(SysLogOperateQuery query);
}