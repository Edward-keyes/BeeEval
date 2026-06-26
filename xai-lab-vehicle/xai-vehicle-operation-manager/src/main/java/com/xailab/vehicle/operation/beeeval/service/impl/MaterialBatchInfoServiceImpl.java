package com.xailab.vehicle.operation.beeeval.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xailab.vehicle.operation.beeeval.dao.MaterialBatchLogInfoDao;
import com.xailab.vehicle.operation.beeeval.entity.MaterialBatchLogInfoEntity;
import com.xailab.vehicle.operation.beeeval.entity.vo.MaterialBatchLogInfoVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xailab.vehicle.operation.beeeval.dao.MaterialBatchInfoDao;
import com.xailab.vehicle.operation.beeeval.entity.MaterialBatchInfoEntity;
import com.xailab.vehicle.operation.beeeval.service.MaterialBatchInfoService;


@Slf4j
@DS("test_platform")
@Service("materialBatchInfoService")
public class MaterialBatchInfoServiceImpl extends ServiceImpl<MaterialBatchInfoDao, MaterialBatchInfoEntity> implements MaterialBatchInfoService {

    @Resource
    MaterialBatchLogInfoDao materialBatchLogInfoDao;

    /**
     * 根据批次号分组，查询所有异步同步数据信息
     * @return
     */
    @Override
    public List<MaterialBatchLogInfoVo> queryAllMaterialBatchInfo() {

        //查询所有异步同步数据详情
        List<MaterialBatchLogInfoEntity> batchLogInfoList = materialBatchLogInfoDao.selectList(new QueryWrapper<>());

        List<MaterialBatchInfoEntity> batchInfoList = this.list();

        return batchInfoList.stream().map(a -> {
            MaterialBatchLogInfoVo vo = new MaterialBatchLogInfoVo();
            vo.setBatchNum(a.getId());
            vo.setBatchName(a.getBatchName());
            vo.setMaterialClassify(a.getMaterialClassify());
            vo.setCreateDate(a.getCreateDate());
            vo.setExecuteDate(a.getExecutionTime());
            vo.setStatus(a.getStatus());
            vo.setMaterialBatchLogInfoEntityList(
                    batchLogInfoList.stream().
                            filter(b -> b.getBatchNumber().equals(a.getId()))
                            .collect(Collectors.toList()));
            return vo;
        }).toList();
    }
}