package com.xailab.vehicle.operation.testplatform.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xailab.vehicle.feign.vo.OpenSourceVo;
import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.feign.vo.TestCaseByFunctionIdVo;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestCaseEntity;
import com.xailab.vehicle.operation.testplatform.query.TestPlatformVehicleTestCaseQuery;
import com.xailab.vehicle.operation.testplatform.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 测试用例总表
 *
 * @author mumu
 * @since 1.0.0 2025-04-16
 */
@Mapper
public interface TestPlatformVehicleTestCaseDao extends BaseDao<TestPlatformVehicleTestCaseEntity> {

    IPage<TestPlatformTestCasePageVo> selectTestCaseByPage(IPage<TestPlatformVehicleTestCaseEntity> page,
            @Param("query") TestPlatformVehicleTestCaseQuery query);

    List<TestPlatformVehicleCorePageVo> selectVehicleCore(@Param("testCaseId") Integer testCaseId,
            @Param("vehicleIds") List<Integer> vehicleIds);

    Double selectVehicleVariance(@Param("testCaseId") Integer testCaseId,
            @Param("vehicleIds") List<Integer> vehicleIds);

    @Select("select testcase_content from test_case")
    List<String> selectTestCaseInfo();

    /**
     * 查询测试用例的ID、内容和功能域ID，用于开源用例映射初始化
     */
    @Select("SELECT id, testcase_content, function_id FROM test_case WHERE is_enable = 1")
    List<java.util.Map<String, Object>> selectTestCaseForMapping();

    IPage<TestPlatformFunctionTreeVo> selectFunctionTreeTag(IPage<TestPlatformVehicleTestCaseEntity> page,
            @Param("type") String type,
            @Param("scenarioId") Integer scenarioId,
            @Param("functionId") Integer functionId,
            @Param("functionEvaluate") String functionEvaluate,
            @Param("dataState") Integer dataState,
            @Param("testRecordId") Integer testRecordId,
            @Param("testCaseId") Integer testCaseId);

    List<TestPlatformFunctionTreeVo> selectFunctionTreeTagList(@Param("scenarioId") Integer scenarioId);

    List<FunctionTreeCaseVo> selectTreeCaseList(@Param("scenarioId") Integer scenarioId,
            @Param("taskDetail") String taskDetail, @Param("scenarioTask") String scenarioTask,
            @Param("functionId") Integer functionId, @Param("testRecordId") Integer testRecordId);

    String findMaxScenarioTask(@Param("firstly") String firstly, @Param("scenarioId") Integer scenarioId);

    String findMaxTaskDetail(@Param("firstly") String firstly, @Param("scenarioId") Integer scenarioId);

    List<TestCaseByFunctionIdVo> caseQueryByFunctionId(@Param("functionId") String functionId);

    List<TestCaseByFunctionIdVo> caseQuery();

    List<TestCaseContentVo> queryTestCaseContent();

    List<Integer> selectIdsByTaskDetail(@Param("taskDetail") String taskDetail);

    @Select("select id from test_case where scenario_id = #{scenarioId}")
    List<Integer> selectIdByScenarioId(@Param("scenarioId") Integer scenarioId);

    List<CaseRelevancyVo> queryRelevancy();

    List<OpenSourceVo> queryOpenSourceSynchronization(@Param("recordId") String recordId);

    List<DomainCaseVo> queryDomainCase();
}