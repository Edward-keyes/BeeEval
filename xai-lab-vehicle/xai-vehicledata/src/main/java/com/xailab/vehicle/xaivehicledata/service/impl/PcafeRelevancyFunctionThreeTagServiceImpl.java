package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xailab.vehicle.xaivehicledata.dao.PcafeRelevancyFunctionThreeTagDao;
import com.xailab.vehicle.xaivehicledata.entity.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.xaivehicledata.service.PcafeRelevancyFunctionThreeTagService;


@Service("pcafeRelevancyFunctionThreeTagService")
public class PcafeRelevancyFunctionThreeTagServiceImpl extends ServiceImpl<PcafeRelevancyFunctionThreeTagDao, PcafeRelevancyFunctionThreeTagEntity> implements PcafeRelevancyFunctionThreeTagService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PcafeRelevancyFunctionThreeTagEntity> page = this.page(
                new Query<PcafeRelevancyFunctionThreeTagEntity>().getPage(params),
                new QueryWrapper<PcafeRelevancyFunctionThreeTagEntity>()
        );

        return new PageUtils(page);
    }

}