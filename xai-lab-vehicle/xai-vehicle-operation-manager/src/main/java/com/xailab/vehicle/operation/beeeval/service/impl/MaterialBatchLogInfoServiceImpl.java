package com.xailab.vehicle.operation.beeeval.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xailab.vehicle.operation.beeeval.dao.MaterialBatchLogInfoDao;
import com.xailab.vehicle.operation.beeeval.entity.MaterialBatchLogInfoEntity;
import com.xailab.vehicle.operation.beeeval.service.MaterialBatchLogInfoService;


@Slf4j
@DS("test_platform")
@Service("materialBatchLogInfoService")
public class MaterialBatchLogInfoServiceImpl extends ServiceImpl<MaterialBatchLogInfoDao, MaterialBatchLogInfoEntity> implements MaterialBatchLogInfoService {


}