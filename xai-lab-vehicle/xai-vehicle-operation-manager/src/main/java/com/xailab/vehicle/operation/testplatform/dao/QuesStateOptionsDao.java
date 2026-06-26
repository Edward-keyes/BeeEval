package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.QuesStateOptionsEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeCaseStateEvaluateInfo;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateOptionInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 问卷选项 选择结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface QuesStateOptionsDao extends BaseDao<QuesStateOptionsEntity> {


    List<QuesStateOptionInfoVO> findOptionsInfo(@Param("quesStateId") Integer quesStateId);

}