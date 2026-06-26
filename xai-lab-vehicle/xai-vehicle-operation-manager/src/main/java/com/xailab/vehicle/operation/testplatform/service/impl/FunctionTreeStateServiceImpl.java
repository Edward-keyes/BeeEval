package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.FunctionTreeStateConvert;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeStateEntity;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeStateQuery;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeStateVO;
import com.xailab.vehicle.operation.testplatform.dao.FunctionTreeStateDao;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreeStateService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能树状态表
 *
 * @author mumu 
 * @since 1.0.0 2025-05-18
 */
@Service
@AllArgsConstructor
public class FunctionTreeStateServiceImpl extends BaseServiceImpl<FunctionTreeStateDao, FunctionTreeStateEntity> implements FunctionTreeStateService {

    @Override
    public PageResult<FunctionTreeStateVO> page(FunctionTreeStateQuery query) {
        IPage<FunctionTreeStateEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(FunctionTreeStateConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<FunctionTreeStateEntity> getWrapper(FunctionTreeStateQuery query){
        LambdaQueryWrapper<FunctionTreeStateEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(FunctionTreeStateVO vo) {
        FunctionTreeStateEntity entity = FunctionTreeStateConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(FunctionTreeStateVO vo) {
        FunctionTreeStateEntity entity = FunctionTreeStateConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}