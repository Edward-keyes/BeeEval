package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformTestRecordVehicleVO;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleTestRecordConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestRecordEntity;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestRecordQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleTestRecordVO;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestRecordDao;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestRecordService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * test_record
 *
 * @author mm 
 * @since 1.0.0 2025-04-17
 */
@Service
@AllArgsConstructor
@DS("test_platform")
public class TestPlatformVehicleTestRecordServiceImpl extends BaseServiceImpl<TestPlatformVehicleTestRecordDao, TestPlatformVehicleTestRecordEntity> implements TestPlatformVehicleTestRecordService {

    @Resource
    private TestPlatformVehicleTestRecordDao testPlatformVehicleTestRecordDao;

    @Override
    public PageResult<TestPlatformVehicleTestRecordVO> page(TestPlatformVehicleTestRecordQuery query) {
        IPage<TestPlatformVehicleTestRecordEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TestPlatformVehicleTestRecordConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TestPlatformVehicleTestRecordEntity> getWrapper(TestPlatformVehicleTestRecordQuery query){
        LambdaQueryWrapper<TestPlatformVehicleTestRecordEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(TestPlatformVehicleTestRecordVO vo) {
        TestPlatformVehicleTestRecordEntity entity = TestPlatformVehicleTestRecordConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TestPlatformVehicleTestRecordVO vo) {
        TestPlatformVehicleTestRecordEntity entity = TestPlatformVehicleTestRecordConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

    @Override
    public List<TestPlatformTestRecordVehicleVO> findAll() {
        return baseMapper.findRecordVehicleList();
    }

}