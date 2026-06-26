package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.ProcessStateEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeCaseStateCheckInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 流程状态结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface ProcessStateDao extends BaseDao<ProcessStateEntity> {

    List<FunctionTreeCaseStateCheckInfo> findProcessStateInfo(@Param("stateId") Integer stateId);
}