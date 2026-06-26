package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaicommon.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;


import com.xailab.vehicle.xaivehicledata.dao.DomainTagDao;
import com.xailab.vehicle.xaivehicledata.entity.DomainTagEntity;
import com.xailab.vehicle.xaivehicledata.service.DomainTagService;


@Service("domainTagService")
public class DomainTagServiceImpl extends ServiceImpl<DomainTagDao, DomainTagEntity> implements DomainTagService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DomainTagEntity> page = this.page(
                new Query<DomainTagEntity>().getPage(params),
                new QueryWrapper<DomainTagEntity>()
        );

        return new PageUtils(page);
    }

}