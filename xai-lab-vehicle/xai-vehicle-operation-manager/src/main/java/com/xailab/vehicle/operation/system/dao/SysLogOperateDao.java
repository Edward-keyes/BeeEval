package com.xailab.vehicle.operation.system.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.system.entity.SysLogOperateEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志
 *

 */
@Mapper
public interface SysLogOperateDao extends BaseDao<SysLogOperateEntity> {

}