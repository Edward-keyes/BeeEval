package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xailab.vehicle.xaivehicledata.dao.FunctionTreeCaseDao;
import com.xailab.vehicle.xaivehicledata.entity.FunctionTreeCaseEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionTreeCaseService;


@Service("functionTreeCaseService")
public class FunctionTreeCaseServiceImpl extends ServiceImpl<FunctionTreeCaseDao, FunctionTreeCaseEntity> implements FunctionTreeCaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FunctionTreeCaseEntity> page = this.page(
                new Query<FunctionTreeCaseEntity>().getPage(params),
                new QueryWrapper<FunctionTreeCaseEntity>()
        );

        return new PageUtils(page);
    }

}