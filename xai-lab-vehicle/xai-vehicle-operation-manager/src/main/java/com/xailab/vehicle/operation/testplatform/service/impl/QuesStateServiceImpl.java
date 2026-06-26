package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.QuesStateConvert;
import com.xailab.vehicle.operation.testplatform.entity.QuesStateEntity;
import com.xailab.vehicle.operation.testplatform.query.QuesStateQuery;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateVO;
import com.xailab.vehicle.operation.testplatform.dao.QuesStateDao;
import com.xailab.vehicle.operation.testplatform.service.QuesStateService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能评价结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class QuesStateServiceImpl extends BaseServiceImpl<QuesStateDao, QuesStateEntity> implements QuesStateService {

    @Override
    public PageResult<QuesStateVO> page(QuesStateQuery query) {
        IPage<QuesStateEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(QuesStateConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<QuesStateEntity> getWrapper(QuesStateQuery query){
        LambdaQueryWrapper<QuesStateEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(QuesStateVO vo) {
        QuesStateEntity entity = QuesStateConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(QuesStateVO vo) {
        QuesStateEntity entity = QuesStateConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}