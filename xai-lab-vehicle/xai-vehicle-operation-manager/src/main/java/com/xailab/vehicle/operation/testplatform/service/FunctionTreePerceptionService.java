package com.xailab.vehicle.operation.testplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreePerceptionEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.PerceptionTreeResponse;
import com.xailab.vehicle.operation.testplatform.vo.PerceptionTreeEditVo;

import java.util.List;

public interface FunctionTreePerceptionService extends BaseService<FunctionTreePerceptionEntity> {
    List<PerceptionTreeResponse> queryPerceptionTree(Integer recodeId);

    Boolean editPerceptionTree(Integer recordId, List<PerceptionTreeEditVo> perceptionTreeEditVoList);
}
