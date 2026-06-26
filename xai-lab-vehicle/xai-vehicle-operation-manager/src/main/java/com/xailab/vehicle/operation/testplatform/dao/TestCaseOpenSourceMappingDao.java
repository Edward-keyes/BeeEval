package com.xailab.vehicle.operation.testplatform.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.operation.testplatform.entity.TestCaseOpenSourceMappingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 测试用例与开源用例关联表DAO
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Mapper
public interface TestCaseOpenSourceMappingDao extends BaseMapper<TestCaseOpenSourceMappingEntity> {

    /**
     * 根据测试用例ID查询关联的开源用例ID列表
     */
    @Select("SELECT beeeval_open_case_id FROM test_case_open_source_mapping WHERE test_case_id = #{testCaseId}")
    List<Integer> selectOpenCaseIdsByTestCaseId(@Param("testCaseId") Integer testCaseId);

    /**
     * 根据开源用例ID查询关联的测试用例ID
     */
    @Select("SELECT test_case_id FROM test_case_open_source_mapping WHERE beeeval_open_case_id = #{openCaseId}")
    Integer selectTestCaseIdByOpenCaseId(@Param("openCaseId") Integer openCaseId);

    /**
     * 根据测试用例ID列表批量查询映射关系
     */
    @Select("<script>" +
            "SELECT test_case_id, beeeval_open_case_id FROM test_case_open_source_mapping WHERE test_case_id IN " +
            "<foreach item='id' collection='testCaseIds' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    List<Map<String, Object>> selectMappingsByTestCaseIds(@Param("testCaseIds") List<Integer> testCaseIds);
}
