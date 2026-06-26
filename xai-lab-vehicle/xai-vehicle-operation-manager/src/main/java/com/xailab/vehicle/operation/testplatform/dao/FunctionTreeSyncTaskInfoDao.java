package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskInfoEntity;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncTaskInfoListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 功能树同步任务详情表
*
* @author mm 
* @since 1.0.0 2025-06-02
*/
@Mapper
public interface FunctionTreeSyncTaskInfoDao extends BaseDao<FunctionTreeSyncTaskInfoEntity> {


    List<FunctionTreeSyncTaskInfoListVO> findFunctionTreeList(@Param("taskSerial") String taskSerial);
}