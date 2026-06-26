package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncTaskInfoVO;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeSyncTaskInfoQuery;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskInfoEntity;

import java.util.List;

/**
 * 功能树同步任务详情表
 *
 * @author mm 
 * @since 1.0.0 2025-06-02
 */
public interface FunctionTreeSyncTaskInfoService extends BaseService<FunctionTreeSyncTaskInfoEntity> {

    PageResult<FunctionTreeSyncTaskInfoVO> page(FunctionTreeSyncTaskInfoQuery query);

    void save(FunctionTreeSyncTaskInfoVO vo);

    void update(FunctionTreeSyncTaskInfoVO vo);

    void delete(List<Long> idList);
}