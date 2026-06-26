package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.feign.vo.TestCaseByFunctionIdVo;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestCaseEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.TestPlatformImportByExcelResultResponse;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestCaseQuery;
import com.xailab.vehicle.operation.testplatform.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 测试用例总表
 *
 * @author mumu 
 * @since 1.0.0 2025-04-16
 */
public interface TestPlatformVehicleTestCaseService extends BaseService<TestPlatformVehicleTestCaseEntity> {

    PageResult<TestPlatformVehicleTestCaseVO> page(TestPlatformVehicleTestCaseQuery query);

    /**
     * 根据 test_case 查询 State （分数详情）
     * @param id
     * @return
     */
    List<TestPlatformTestStateQueryResVo> findTestStateInfo(Integer id);

    /**
     * 获取当前 test_case 的指标树
     * @return
     */
    List<TestPlatformTestCaseTreeVo> findTestCasIndexTree();

    /**
     * 查询所有测试场景
     * @return
     */
    List<TestPlatformTestScenarioResVo> findTestScenarioList();


    /**
     * 批量excel导入
     * @param file
     */
    TestPlatformImportByExcelResultResponse importByExcel(MultipartFile file);

    void save(TestPlatformVehicleTestCaseAddVO vo);

    void update(TestPlatformVehicleTestCaseUpdateVO vo);

    void delete(List<Long> idList);

    List<TestCaseByFunctionIdVo> caseQueryByFunctionId(String functionId);

    List<TestCaseByFunctionIdVo> caseQuery();

    TestPlatformVehicleTestCaseEntity selectById(Integer testCaseId);

    void queryRelevancySynchronization();

    void queryOpenSourceSynchronization(String recordId,String beeevalVehicleId);
}