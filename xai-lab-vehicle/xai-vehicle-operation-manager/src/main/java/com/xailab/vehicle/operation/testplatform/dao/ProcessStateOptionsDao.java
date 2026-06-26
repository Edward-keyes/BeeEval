package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.ProcessStateOptionsEntity;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessOptionsStateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 流程多选项 选择结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface ProcessStateOptionsDao extends BaseDao<ProcessStateOptionsEntity> {

    List<TestProcessOptionsStateVO> findProcessStateOptionInfo(@Param("processStateId") Integer processStateId);
}