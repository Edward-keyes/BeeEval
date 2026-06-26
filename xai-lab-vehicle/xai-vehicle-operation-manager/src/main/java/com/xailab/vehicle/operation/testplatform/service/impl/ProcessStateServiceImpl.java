package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.ProcessStateConvert;
import com.xailab.vehicle.operation.testplatform.entity.ProcessStateEntity;
import com.xailab.vehicle.operation.testplatform.query.ProcessStateQuery;
import com.xailab.vehicle.operation.testplatform.vo.ProcessStateVO;
import com.xailab.vehicle.operation.testplatform.dao.ProcessStateDao;
import com.xailab.vehicle.operation.testplatform.service.ProcessStateService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程状态结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class ProcessStateServiceImpl extends BaseServiceImpl<ProcessStateDao, ProcessStateEntity> implements ProcessStateService {

    @Override
    public PageResult<ProcessStateVO> page(ProcessStateQuery query) {
        IPage<ProcessStateEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(ProcessStateConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<ProcessStateEntity> getWrapper(ProcessStateQuery query){
        LambdaQueryWrapper<ProcessStateEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(ProcessStateVO vo) {
        ProcessStateEntity entity = ProcessStateConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(ProcessStateVO vo) {
        ProcessStateEntity entity = ProcessStateConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}