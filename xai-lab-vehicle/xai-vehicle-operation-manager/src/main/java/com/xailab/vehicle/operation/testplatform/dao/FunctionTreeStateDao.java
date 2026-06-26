package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeStateEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 功能树状态表
*
* @author mumu 
* @since 1.0.0 2025-05-18
*/
@Mapper
public interface FunctionTreeStateDao extends BaseDao<FunctionTreeStateEntity> {

    List<FunctionTreeInfoResponse> queryInfo(@Param("taskDetail") String taskDetail);
}