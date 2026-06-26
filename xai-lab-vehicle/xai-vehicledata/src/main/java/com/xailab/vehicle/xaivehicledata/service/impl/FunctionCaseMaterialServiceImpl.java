package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xailab.vehicle.xaivehicledata.dao.FunctionCaseMaterialDao;
import com.xailab.vehicle.xaivehicledata.entity.FunctionCaseMaterialEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionCaseMaterialService;


@Service("functionCaseMaterialService")
public class FunctionCaseMaterialServiceImpl extends ServiceImpl<FunctionCaseMaterialDao, FunctionCaseMaterialEntity> implements FunctionCaseMaterialService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FunctionCaseMaterialEntity> page = this.page(
                new Query<FunctionCaseMaterialEntity>().getPage(params),
                new QueryWrapper<FunctionCaseMaterialEntity>()
        );

        return new PageUtils(page);
    }

}