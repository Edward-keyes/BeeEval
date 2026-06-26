package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 流程数据表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface TestProcessDao extends BaseDao<TestProcessEntity> {
	
}