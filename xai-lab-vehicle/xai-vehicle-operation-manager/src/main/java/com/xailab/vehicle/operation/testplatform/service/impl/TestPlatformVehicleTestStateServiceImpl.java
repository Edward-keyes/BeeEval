package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xailab.vehicle.operation.testplatform.pojo.request.QueryStateDataVehicleIdRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.StateDataResponse;
import com.xailab.vehicle.operation.testplatform.vo.ScoreIdVo;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleTestStateConvert;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestStateQuery;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleTestStateVO;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestStateDao;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestStateService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 测试记录用例表
 *
 * @author mumu 
 * @since 1.0.0 2025-04-16
 */
@Service
@DS("test_platform")
@AllArgsConstructor
public class TestPlatformVehicleTestStateServiceImpl extends BaseServiceImpl<TestPlatformVehicleTestStateDao, TestPlatformVehicleTestStateEntity> implements TestPlatformVehicleTestStateService {

    @Resource
    private TestPlatformVehicleTestStateDao testPlatformVehicleTestStateDao;

    @Override
    public PageResult<TestPlatformVehicleTestStateVO> page(TestPlatformVehicleTestStateQuery query) {
        IPage<TestPlatformVehicleTestStateEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(TestPlatformVehicleTestStateConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<TestPlatformVehicleTestStateEntity> getWrapper(TestPlatformVehicleTestStateQuery query){
        LambdaQueryWrapper<TestPlatformVehicleTestStateEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(TestPlatformVehicleTestStateVO vo) {
        TestPlatformVehicleTestStateEntity entity = TestPlatformVehicleTestStateConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(TestPlatformVehicleTestStateVO vo) {
        TestPlatformVehicleTestStateEntity entity = TestPlatformVehicleTestStateConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

    @Override
    public TestPlatformVehicleTestStateEntity queryOneTestStateByCaseIdAndVehicleId(String caseId, Integer vehicleId) {

        return testPlatformVehicleTestStateDao.queryOneTestStateByCaseIdAndVehicleId(caseId,vehicleId);
    }

    @Override
    public void updateScore(List<ScoreIdVo> id) {
        for (ScoreIdVo scoreIdVo : id) {
            testPlatformVehicleTestStateDao.updateScore(scoreIdVo.getId(), scoreIdVo.getIsSuccessful(), scoreIdVo.getScore());
        }
    }

    @Override
    public void insertOrUpdate(TestPlatformVehicleTestStateEntity stateEntity) {
        testPlatformVehicleTestStateDao.insertOrUpdate(stateEntity);
    }

    @Override
    public List<StateDataResponse> queryStateData(Integer vehicleId) {
        return testPlatformVehicleTestStateDao.queryStateData(vehicleId);
    }

    @Override
    public IPage<StateDataResponse> selectTestStatePage(Integer pageNum, Integer pageSize, List<Integer> vehicleId, QueryStateDataVehicleIdRequest request) {
        Page<StateDataResponse> page = new Page<>(pageNum, pageSize);
        return testPlatformVehicleTestStateDao.selectTestStatePage(
                page,
                vehicleId,
                request.getTestcaseContent(),
                request.getIsSuccessful(),
                request.getScore(),
                request.getTestStatus(),
                request.getFunctionName(),
                request.getCaseType(),
                request.getErrorType(),
                request.getCaseId(),
                request.getThreeTag(),
                request.getErrorDetail()
        );
    }


}