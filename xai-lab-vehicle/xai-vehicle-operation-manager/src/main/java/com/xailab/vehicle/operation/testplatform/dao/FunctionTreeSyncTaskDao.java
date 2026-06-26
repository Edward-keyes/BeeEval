package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 功能树数据同步表
*
* @author mumu 
* @since 1.0.0 2025-06-02
*/
@Mapper
public interface FunctionTreeSyncTaskDao extends BaseDao<FunctionTreeSyncTaskEntity> {
	
}