package com.xailab.vehicle.operation.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.system.convert.SysSmsLogConvert;
import com.xailab.vehicle.operation.system.dao.SysSmsLogDao;
import com.xailab.vehicle.operation.system.entity.SysSmsLogEntity;
import com.xailab.vehicle.operation.system.query.SysSmsLogQuery;
import com.xailab.vehicle.operation.system.service.SysSmsLogService;
import com.xailab.vehicle.operation.system.vo.SysSmsLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 短信日志
 *

 */
@Service
@AllArgsConstructor
public class SysSmsLogServiceImpl extends BaseServiceImpl<SysSmsLogDao, SysSmsLogEntity> implements SysSmsLogService {

    @Override
    public PageResult<SysSmsLogVO> page(SysSmsLogQuery query) {
        IPage<SysSmsLogEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(SysSmsLogConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }


    private LambdaQueryWrapper<SysSmsLogEntity> getWrapper(SysSmsLogQuery query) {
        LambdaQueryWrapper<SysSmsLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(query.getPlatform() != null, SysSmsLogEntity::getPlatform, query.getPlatform());
        wrapper.like(StrUtil.isNotBlank(query.getMobile()), SysSmsLogEntity::getMobile, query.getMobile());
        wrapper.orderByDesc(SysSmsLogEntity::getId);
        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}