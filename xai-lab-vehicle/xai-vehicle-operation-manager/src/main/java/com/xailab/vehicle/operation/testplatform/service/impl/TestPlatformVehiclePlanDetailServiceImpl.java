package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehiclePlanDetailConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehiclePlanDetailEntity;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehiclePlanDetailQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehiclePlanDetailVO;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehiclePlanDetailDao;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehiclePlanDetailService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 方案细分表
 *
 * @author mumu
 * @since 1.0.0 2025-04-16
 */
@Service
@AllArgsConstructor
@DS("test_platform")
public class TestPlatformVehiclePlanDetailServiceImpl extends BaseServiceImpl<TestPlatformVehiclePlanDetailDao, TestPlatformVehiclePlanDetailEntity> implements TestPlatformVehiclePlanDetailService {

    @Override
    public PageResult<TestPlatformVehiclePlanDetailVO> page(TestPlatformVehiclePlanDetailQuery query) {
        IPage<TestPlatformVehiclePlanDetailEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TestPlatformVehiclePlanDetailConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TestPlatformVehiclePlanDetailEntity> getWrapper(TestPlatformVehiclePlanDetailQuery query){
        LambdaQueryWrapper<TestPlatformVehiclePlanDetailEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(TestPlatformVehiclePlanDetailVO vo) {
        TestPlatformVehiclePlanDetailEntity entity = TestPlatformVehiclePlanDetailConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TestPlatformVehiclePlanDetailVO vo) {
        TestPlatformVehiclePlanDetailEntity entity = TestPlatformVehiclePlanDetailConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}