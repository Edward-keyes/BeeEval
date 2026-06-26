package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestQuesConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestQuesEntity;
import com.xailab.vehicle.operation.testplatform.query.TestQuesQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesVO;
import com.xailab.vehicle.operation.testplatform.dao.TestQuesDao;
import com.xailab.vehicle.operation.testplatform.service.TestQuesService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能评价数据表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class TestQuesServiceImpl extends BaseServiceImpl<TestQuesDao, TestQuesEntity> implements TestQuesService {

    @Override
    public PageResult<TestQuesVO> page(TestQuesQuery query) {
        IPage<TestQuesEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TestQuesConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TestQuesEntity> getWrapper(TestQuesQuery query){
        LambdaQueryWrapper<TestQuesEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(TestQuesVO vo) {
        TestQuesEntity entity = TestQuesConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TestQuesVO vo) {
        TestQuesEntity entity = TestQuesConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}