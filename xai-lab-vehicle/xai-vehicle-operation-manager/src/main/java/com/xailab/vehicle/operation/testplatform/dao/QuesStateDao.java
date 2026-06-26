package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.QuesStateEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeCaseStateEvaluateInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 功能评价结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface QuesStateDao extends BaseDao<QuesStateEntity> {

    List<FunctionTreeCaseStateEvaluateInfo> queryQuesStateInfo(@Param("testStateId") Integer testStateId);

}