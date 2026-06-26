package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleInfoConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleInfoEntity;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleInfoQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleInfoVO;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleInfoDao;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleInfoService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆信息表
 *
 * @author mm 
 * @since 1.0.0 2025-04-17
 */
@Service
@AllArgsConstructor
@DS("test_platform")
public class TestPlatformVehicleInfoServiceImpl extends BaseServiceImpl<TestPlatformVehicleInfoDao, TestPlatformVehicleInfoEntity> implements TestPlatformVehicleInfoService {

    @Override
    public PageResult<TestPlatformVehicleInfoVO> page(TestPlatformVehicleInfoQuery query) {
        IPage<TestPlatformVehicleInfoEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TestPlatformVehicleInfoConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    @Override
    public List<TestPlatformVehicleInfoVO> getVehicleList() {
        List<TestPlatformVehicleInfoEntity> testPlatformVehicleInfoEntities = baseMapper.selectList(Wrappers.<TestPlatformVehicleInfoEntity>lambdaQuery());
        return TestPlatformVehicleInfoConvert.INSTANCE.convertList(testPlatformVehicleInfoEntities);
    }

    private LambdaQueryWrapper<TestPlatformVehicleInfoEntity> getWrapper(TestPlatformVehicleInfoQuery query){
        LambdaQueryWrapper<TestPlatformVehicleInfoEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(TestPlatformVehicleInfoVO vo) {
        TestPlatformVehicleInfoEntity entity = TestPlatformVehicleInfoConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TestPlatformVehicleInfoVO vo) {
        TestPlatformVehicleInfoEntity entity = TestPlatformVehicleInfoConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}