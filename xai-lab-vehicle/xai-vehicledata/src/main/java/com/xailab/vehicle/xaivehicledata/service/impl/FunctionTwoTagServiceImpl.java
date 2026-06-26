package com.xailab.vehicle.xaivehicledata.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.Query;

import com.xailab.vehicle.xaivehicledata.dao.FunctionTwoTagDao;
import com.xailab.vehicle.xaivehicledata.entity.FunctionTwoTagEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionTwoTagService;


@Service("functionTwoTagService")
public class FunctionTwoTagServiceImpl extends ServiceImpl<FunctionTwoTagDao, FunctionTwoTagEntity> implements FunctionTwoTagService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FunctionTwoTagEntity> page = this.page(
                new Query<FunctionTwoTagEntity>().getPage(params),
                new QueryWrapper<FunctionTwoTagEntity>()
        );

        return new PageUtils(page);
    }

}