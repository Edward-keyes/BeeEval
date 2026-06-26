package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeListResponse;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationRequest;
import com.xailab.vehicle.feign.vo.FunctionTreeSynchronizationVoRequest;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaivehicledata.entity.FunctionThreeTagEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.SortRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionTreeOpResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.SynchronizationThreeTagResponse;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * 
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
public interface FunctionThreeTagService extends IService<FunctionThreeTagEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String, Long> getAllIdAndTagNumber();

    List<FunctionTreeOpResponse> getFunctionTagList();

    Integer sortThreeTag(SortRequest sortRequest);

    List<SynchronizationThreeTagResponse> getThreeTagListSynchronization();

    /**
     * 查询功能数list
     * @return
     */
    List<FunctionTreeListResponse> findFunctionTreeList();

    Boolean syncToBeeeval(FunctionTreeSynchronizationVoRequest request);
}

