package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestQuesOptionsConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestQuesOptionsEntity;
import com.xailab.vehicle.operation.testplatform.query.TestQuesOptionsQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesOptionsVO;
import com.xailab.vehicle.operation.testplatform.dao.TestQuesOptionsDao;
import com.xailab.vehicle.operation.testplatform.service.TestQuesOptionsService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能评价选项表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class TestQuesOptionsServiceImpl extends BaseServiceImpl<TestQuesOptionsDao, TestQuesOptionsEntity> implements TestQuesOptionsService {

    @Override
    public PageResult<TestQuesOptionsVO> page(TestQuesOptionsQuery query) {
        IPage<TestQuesOptionsEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TestQuesOptionsConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TestQuesOptionsEntity> getWrapper(TestQuesOptionsQuery query){
        LambdaQueryWrapper<TestQuesOptionsEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(TestQuesOptionsVO vo) {
        TestQuesOptionsEntity entity = TestQuesOptionsConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TestQuesOptionsVO vo) {
        TestQuesOptionsEntity entity = TestQuesOptionsConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}