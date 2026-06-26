package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.ProcessStateOptionsConvert;
import com.xailab.vehicle.operation.testplatform.entity.ProcessStateOptionsEntity;
import com.xailab.vehicle.operation.testplatform.query.ProcessStateOptionsQuery;
import com.xailab.vehicle.operation.testplatform.vo.ProcessStateOptionsVO;
import com.xailab.vehicle.operation.testplatform.dao.ProcessStateOptionsDao;
import com.xailab.vehicle.operation.testplatform.service.ProcessStateOptionsService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程多选项 选择结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class ProcessStateOptionsServiceImpl extends BaseServiceImpl<ProcessStateOptionsDao, ProcessStateOptionsEntity> implements ProcessStateOptionsService {

    @Override
    public PageResult<ProcessStateOptionsVO> page(ProcessStateOptionsQuery query) {
        IPage<ProcessStateOptionsEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(ProcessStateOptionsConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<ProcessStateOptionsEntity> getWrapper(ProcessStateOptionsQuery query){
        LambdaQueryWrapper<ProcessStateOptionsEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(ProcessStateOptionsVO vo) {
        ProcessStateOptionsEntity entity = ProcessStateOptionsConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(ProcessStateOptionsVO vo) {
        ProcessStateOptionsEntity entity = ProcessStateOptionsConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}