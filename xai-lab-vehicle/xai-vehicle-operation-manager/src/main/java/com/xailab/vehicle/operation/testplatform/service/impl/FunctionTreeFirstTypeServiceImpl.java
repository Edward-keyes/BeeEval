package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.FunctionTreeFirstTypeConvert;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeFirstTypeEntity;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeFirstTypeQuery;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeFirstTypeVO;
import com.xailab.vehicle.operation.testplatform.dao.FunctionTreeFirstTypeDao;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreeFirstTypeService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能树一级标签类型
 *
 * @author mu 
 * @since  2025-05-31
 */
@Service
@AllArgsConstructor
@DS("test_platform")
public class FunctionTreeFirstTypeServiceImpl extends BaseServiceImpl<FunctionTreeFirstTypeDao, FunctionTreeFirstTypeEntity> implements FunctionTreeFirstTypeService {

    @Override
    public PageResult<FunctionTreeFirstTypeVO> page(FunctionTreeFirstTypeQuery query) {
        IPage<FunctionTreeFirstTypeEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(FunctionTreeFirstTypeConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<FunctionTreeFirstTypeEntity> getWrapper(FunctionTreeFirstTypeQuery query){
        LambdaQueryWrapper<FunctionTreeFirstTypeEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(FunctionTreeFirstTypeVO vo) {
        FunctionTreeFirstTypeEntity entity = FunctionTreeFirstTypeConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(FunctionTreeFirstTypeVO vo) {
        FunctionTreeFirstTypeEntity entity = FunctionTreeFirstTypeConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}