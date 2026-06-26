package com.xailab.vehicle.operation.system.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.system.entity.SysLogLoginEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志
 *

 */
@Mapper
public interface SysLogLoginDao extends BaseDao<SysLogLoginEntity> {

}