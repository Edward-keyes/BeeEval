package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.QuesStateOptionsConvert;
import com.xailab.vehicle.operation.testplatform.entity.QuesStateOptionsEntity;
import com.xailab.vehicle.operation.testplatform.query.QuesStateOptionsQuery;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateOptionsVO;
import com.xailab.vehicle.operation.testplatform.dao.QuesStateOptionsDao;
import com.xailab.vehicle.operation.testplatform.service.QuesStateOptionsService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 问卷选项 选择结果
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class QuesStateOptionsServiceImpl extends BaseServiceImpl<QuesStateOptionsDao, QuesStateOptionsEntity> implements QuesStateOptionsService {

    @Override
    public PageResult<QuesStateOptionsVO> page(QuesStateOptionsQuery query) {
        IPage<QuesStateOptionsEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(QuesStateOptionsConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<QuesStateOptionsEntity> getWrapper(QuesStateOptionsQuery query){
        LambdaQueryWrapper<QuesStateOptionsEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(QuesStateOptionsVO vo) {
        QuesStateOptionsEntity entity = QuesStateOptionsConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(QuesStateOptionsVO vo) {
        QuesStateOptionsEntity entity = QuesStateOptionsConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}