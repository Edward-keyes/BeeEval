package com.xailab.vehicle.operation.beeeval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.operation.beeeval.entity.MaterialBatchInfoEntity;
import com.xailab.vehicle.operation.beeeval.entity.vo.MaterialBatchLogInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-07-10 21:29:52
 *
 */
public interface MaterialBatchInfoService extends IService<MaterialBatchInfoEntity> {

    /**
     * 根据批次号分组，查询所有异步同步数据信息
     */
    public List<MaterialBatchLogInfoVo> queryAllMaterialBatchInfo();

}

