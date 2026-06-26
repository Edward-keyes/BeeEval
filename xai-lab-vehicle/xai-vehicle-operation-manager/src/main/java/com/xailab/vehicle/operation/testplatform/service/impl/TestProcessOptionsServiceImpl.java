package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestProcessOptionsConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessOptionsEntity;
import com.xailab.vehicle.operation.testplatform.query.TestProcessOptionsQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessOptionsVO;
import com.xailab.vehicle.operation.testplatform.dao.TestProcessOptionsDao;
import com.xailab.vehicle.operation.testplatform.service.TestProcessOptionsService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程选项表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class TestProcessOptionsServiceImpl extends BaseServiceImpl<TestProcessOptionsDao, TestProcessOptionsEntity> implements TestProcessOptionsService {

    @Override
    public PageResult<TestProcessOptionsVO> page(TestProcessOptionsQuery query) {
        IPage<TestProcessOptionsEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TestProcessOptionsConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TestProcessOptionsEntity> getWrapper(TestProcessOptionsQuery query){
        LambdaQueryWrapper<TestProcessOptionsEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(TestProcessOptionsVO vo) {
        TestProcessOptionsEntity entity = TestProcessOptionsConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TestProcessOptionsVO vo) {
        TestProcessOptionsEntity entity = TestProcessOptionsConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}