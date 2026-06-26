package com.xailab.vehicle.operation.testplatform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.pojo.request.QueryStateDataVehicleIdRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.StateDataResponse;
import com.xailab.vehicle.operation.testplatform.vo.ScoreIdVo;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleTestStateVO;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestStateQuery;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;

import java.util.List;

/**
 * 测试记录用例表
 *
 * @author mumu 
 * @since 1.0.0 2025-04-16
 */
public interface TestPlatformVehicleTestStateService extends BaseService<TestPlatformVehicleTestStateEntity> {

    PageResult<TestPlatformVehicleTestStateVO> page(TestPlatformVehicleTestStateQuery query);

    void save(TestPlatformVehicleTestStateVO vo);

    void update(TestPlatformVehicleTestStateVO vo);

    void delete(List<Long> idList);

    TestPlatformVehicleTestStateEntity queryOneTestStateByCaseIdAndVehicleId(String caseId, Integer vehicleId);

    void updateScore(List<ScoreIdVo> id);

    void insertOrUpdate(TestPlatformVehicleTestStateEntity stateEntity);

    List<StateDataResponse> queryStateData(Integer vehicleId);

    public IPage<StateDataResponse> selectTestStatePage(Integer pageNum, Integer pageSize, List<Integer> vehicleId, QueryStateDataVehicleIdRequest request);
}