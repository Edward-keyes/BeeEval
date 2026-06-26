package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.ErrorTypeConvert;
import com.xailab.vehicle.operation.testplatform.entity.ErrorTypeEntity;
import com.xailab.vehicle.operation.testplatform.query.ErrorTypeQuery;
import com.xailab.vehicle.operation.testplatform.vo.ErrorTypeVO;
import com.xailab.vehicle.operation.testplatform.dao.ErrorTypeDao;
import com.xailab.vehicle.operation.testplatform.service.ErrorTypeService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class ErrorTypeServiceImpl extends BaseServiceImpl<ErrorTypeDao, ErrorTypeEntity> implements ErrorTypeService {

    @Override
    public PageResult<ErrorTypeVO> page(ErrorTypeQuery query) {
        IPage<ErrorTypeEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(ErrorTypeConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<ErrorTypeEntity> getWrapper(ErrorTypeQuery query){
        LambdaQueryWrapper<ErrorTypeEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(ErrorTypeVO vo) {
        ErrorTypeEntity entity = ErrorTypeConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(ErrorTypeVO vo) {
        ErrorTypeEntity entity = ErrorTypeConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}