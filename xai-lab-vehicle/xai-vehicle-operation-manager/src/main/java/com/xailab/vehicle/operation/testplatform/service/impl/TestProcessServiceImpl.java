package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestProcessConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessEntity;
import com.xailab.vehicle.operation.testplatform.query.TestProcessQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessVO;
import com.xailab.vehicle.operation.testplatform.dao.TestProcessDao;
import com.xailab.vehicle.operation.testplatform.service.TestProcessService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程数据表
 *
 * @author 阿沐 babamu@126.com
 * @since 1.0.0 2025-04-26
 */
@Service
@AllArgsConstructor
public class TestProcessServiceImpl extends BaseServiceImpl<TestProcessDao, TestProcessEntity> implements TestProcessService {

    @Override
    public PageResult<TestProcessVO> page(TestProcessQuery query) {
        IPage<TestProcessEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TestProcessConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TestProcessEntity> getWrapper(TestProcessQuery query){
        LambdaQueryWrapper<TestProcessEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(TestProcessVO vo) {
        TestProcessEntity entity = TestProcessConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TestProcessVO vo) {
        TestProcessEntity entity = TestProcessConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}