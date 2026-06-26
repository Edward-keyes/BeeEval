package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.feign.vo.FunctionDomainIndexVo;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.entity.PcafeRelevancyFunctionDomainEntity;

public interface PcafeRelevancyFunctionDomainService extends BaseService<PcafeRelevancyFunctionDomainEntity> {

    /**
     * 基于两边纯文本来进行关联同步
     */
    public Boolean syncFunctionDomain();

}