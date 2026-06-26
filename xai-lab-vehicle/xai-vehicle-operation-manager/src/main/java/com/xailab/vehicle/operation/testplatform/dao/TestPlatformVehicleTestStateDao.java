package com.xailab.vehicle.operation.testplatform.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.StateDataResponse;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformTestStateQueryResVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

import java.util.List;

/**
 * 测试记录用例表
 *
 * @author mumu
 * @since 1.0.0 2025-04-16
 */
@Mapper
public interface TestPlatformVehicleTestStateDao extends BaseDao<TestPlatformVehicleTestStateEntity> {

    List<TestPlatformTestStateQueryResVo> selectAllAndVehicleByCaseId(@Param("testcaseId") Integer testcaseId);

    TestPlatformVehicleTestStateEntity queryOneTestStateByCaseIdAndVehicleId(@Param("caseId") String caseId,
            @Param("vehicleId") Integer vehicleId);

    void updateScore(@Param("id") Integer id, @Param("isSuccessful") Integer isSuccessful,
            @Param("score") Integer score);

    List<StateDataResponse> queryStateData(@Param("vehicleId") Integer vehicleId);

    IPage<StateDataResponse> selectTestStatePage(
            @Param("page") IPage<StateDataResponse> page,
            @Param("vehicleIds") List<Integer> vehicleId,
            @Param("testcaseContent") String testcaseContent,
            @Param("isSuccessful") Integer isSuccessful,
            @Param("scores") List<Integer> score,
            @Param("testStatus") Integer testStatus,
            @Param("functionName") String functionName,
            @Param("caseType") Integer caseType,
            @Param("errorType") Integer errorType,
            @Param("caseId") Integer caseId,
            @Param("threeTag") String threeTag,
            @Param("errorDetail") String errorDetail);

    /**
     * 根据测试记录ID查询所有测试状态数据（用于开源题目分数同步）
     */
    @Select("SELECT testcase_id, score FROM test_state WHERE record_id = #{recordId} AND score > 0")
    List<Map<String, Object>> selectTestStatesByRecordId(@Param("recordId") Integer recordId);
}