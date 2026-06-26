package com.xailab.vehicle.operation.beeeval.service;

import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.feign.vo.SynchronizationThreeTagResponse;
import com.xailab.vehicle.operation.beeeval.entity.vo.TestCaseThreeTagVo;

import java.util.List;

public interface PcafeThreeTagService  {

    /**
     * 获取pcafe三级标签List
     * @return
     */

    List<TestCaseThreeTagVo> getPcafeThreeTagList();

    Boolean associationByThreeTagNumber(List<SynchronizationThreeTagResponse> threeTagListSynchronization );

    List<PcafeRelevancyFunctionThreeTagEntity> queryList();
}
