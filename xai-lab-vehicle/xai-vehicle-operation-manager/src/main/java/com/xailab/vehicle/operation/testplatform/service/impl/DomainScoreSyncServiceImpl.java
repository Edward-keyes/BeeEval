package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.vehicledata.VehicleDataSyncFeign;
import com.xailab.vehicle.feign.vo.BasicAbilityScoreSyncVO;
import com.xailab.vehicle.feign.vo.DomainScoreSyncVO;
import com.xailab.vehicle.feign.vo.TertiaryMetricScoreSyncVO;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.dao.PcafeFunctionDomainMappingDao;
import com.xailab.vehicle.operation.testplatform.dao.PcafeRelevancyFunctionDomainDao;
import com.xailab.vehicle.operation.testplatform.dao.TertiaryMetricWeightDao;
import com.xailab.vehicle.operation.testplatform.dao.TestCaseOpenSourceMappingDao;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestStateDao;
import com.xailab.vehicle.operation.testplatform.dao.TestCaseOpenSourceMappingDao;
import com.xailab.vehicle.operation.testplatform.entity.PcafeFunctionDomainMappingEntity;
import com.xailab.vehicle.operation.testplatform.entity.PcafeRelevancyFunctionDomainEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestRecordEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.TertiaryMetricWeightResponse;
import com.xailab.vehicle.operation.testplatform.service.DomainScoreSyncService;
import com.xailab.vehicle.operation.testplatform.service.TertiaryMetricWeightService;
import com.xailab.vehicle.operation.testplatform.service.TestPlatformVehicleTestRecordService;
import com.xailab.vehicle.operation.testplatform.vo.BaseFunctionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 功能域分数同步服务实现
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Slf4j
@Service("domainScoreSyncService")
@DS("test_platform")
public class DomainScoreSyncServiceImpl implements DomainScoreSyncService {

    @Autowired
    private TertiaryMetricWeightDao tertiaryMetricWeightDao;

    @Autowired
    private PcafeRelevancyFunctionDomainDao pcafeRelevancyFunctionDomainDao;

    @Autowired
    private PcafeFunctionDomainMappingDao pcafeFunctionDomainMappingDao;

    @Autowired
    private TestCaseOpenSourceMappingDao testCaseOpenSourceMappingDao;

    @Autowired
    private TestPlatformVehicleTestRecordService testPlatformVehicleTestRecordService;

    @Autowired
    private TestPlatformVehicleTestStateDao testPlatformVehicleTestStateDao;

    @Autowired
    private VehicleDataSyncFeign vehicleDataSyncFeign;

    @Autowired
    private TertiaryMetricWeightService tertiaryMetricWeightService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result syncDomainScore(Integer testRecordId, Long vehicleBaseInfoId) {
        try {
            log.info("开始同步功能域分数，testRecordId: {}, vehicleBaseInfoId: {}", testRecordId, vehicleBaseInfoId);

            // 查询功能域分数数据
            List<DomainScoreSyncVO> syncData = queryDomainScoreData(testRecordId, vehicleBaseInfoId);

            if (CollectionUtils.isEmpty(syncData)) {
                log.warn("没有找到需要同步的功能域分数数据");
                return Result.ok();
            }

            // 调用BeeEval服务同步数据
            com.xailab.vehicle.feign.common.Result<Void> result = vehicleDataSyncFeign.syncDomainScore(syncData);

            if (result.getCode() == 0) {
                log.info("功能域分数同步成功，同步了{}条数据", syncData.size());
                return Result.ok();
            } else {
                log.error("功能域分数同步失败: {}", result.getMsg());
                throw new RuntimeException("功能域分数同步失败: " + result.getMsg());
            }

        } catch (Exception e) {
            log.error("同步功能域分数时发生异常", e);
            throw new RuntimeException("同步功能域分数失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> syncTertiaryMetricScore(Integer testRecordId, Long vehicleBaseInfoId) {
        try {
            log.info("开始同步三级指标分数，testRecordId: {}, vehicleBaseInfoId: {}", testRecordId, vehicleBaseInfoId);

            // 查询三级指标分数数据
            List<TertiaryMetricScoreSyncVO> syncData = queryTertiaryMetricScoreData(testRecordId, vehicleBaseInfoId);

            if (CollectionUtils.isEmpty(syncData)) {
                log.warn("没有找到需要同步的三级指标分数数据");
                return Result.ok();
            }

            // 调用BeeEval服务同步数据
            com.xailab.vehicle.feign.common.Result<Void> result = vehicleDataSyncFeign
                    .syncTertiaryMetricScore(syncData);

            if (result.getCode() == 0) {
                log.info("三级指标分数同步成功，同步了{}条数据", syncData.size());
                return Result.ok();
            } else {
                log.error("三级指标分数同步失败: {}", result.getMsg());
                throw new RuntimeException("三级指标分数同步失败: " + result.getMsg());
            }

        } catch (Exception e) {
            log.error("同步三级指标分数时发生异常", e);
            throw new RuntimeException("同步三级指标分数失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> syncBasicAbilityScore(Integer testRecordId, Long vehicleBaseInfoId) {
        try {
            log.info("开始同步基础能力分数，testRecordId: {}, vehicleBaseInfoId: {}", testRecordId, vehicleBaseInfoId);

            // 查询基础能力分数数据
            List<BasicAbilityScoreSyncVO> syncData = queryBasicAbilityScoreData(testRecordId, vehicleBaseInfoId);

            if (CollectionUtils.isEmpty(syncData)) {
                log.warn("没有找到需要同步的基础能力分数数据");
                return Result.ok();
            }

            // 调用BeeEval服务同步数据
            com.xailab.vehicle.feign.common.Result<Void> result = vehicleDataSyncFeign.syncBasicAbilityScore(syncData);

            if (result.getCode() == 0) {
                log.info("基础能力分数同步成功，同步了{}条数据", syncData.size());
                return Result.ok();
            } else {
                log.error("基础能力分数同步失败: {}", result.getMsg());
                throw new RuntimeException("基础能力分数同步失败: " + result.getMsg());
            }

        } catch (Exception e) {
            log.error("同步基础能力分数时发生异常", e);
            throw new RuntimeException("同步基础能力分数失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result syncOpenSourceScore(Integer testRecordId, Long vehicleBaseInfoId) {
        try {
            log.info("开始同步开源题目分数，testRecordId: {}, vehicleBaseInfoId: {}", testRecordId, vehicleBaseInfoId);

            // 获取测试记录对应的车辆信息
            TestPlatformVehicleTestRecordEntity testRecord = testPlatformVehicleTestRecordService.getById(testRecordId);
            if (testRecord == null) {
                log.warn("测试记录不存在，testRecordId: {}", testRecordId);
                return Result.ok();
            }

            // 查询该测试记录下的所有测试状态（包含分数）
            List<Map<String, Object>> testStates = getTestStatesByRecordId(testRecordId);

            if (CollectionUtils.isEmpty(testStates)) {
                log.warn("没有找到测试状态数据，testRecordId: {}", testRecordId);
                return Result.ok();
            }

            // 提取所有测试用例ID，用于批量查询映射关系
            List<Integer> testCaseIds = testStates.stream()
                    .map(state -> (Integer) state.get("testcase_id"))
                    .filter(id -> id != null)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());

            if (CollectionUtils.isEmpty(testCaseIds)) {
                log.warn("没有有效的测试用例ID，testRecordId: {}", testRecordId);
                return Result.ok();
            }

            // 批量查询映射关系
            List<Map<String, Object>> mappings = testCaseOpenSourceMappingDao.selectMappingsByTestCaseIds(testCaseIds);

            // 构建testCaseId -> openCaseId的映射
            Map<Integer, Integer> testCaseToOpenCaseMap = mappings.stream()
                    .collect(java.util.stream.Collectors.toMap(
                            mapping -> (Integer) mapping.get("test_case_id"),
                            mapping -> (Integer) mapping.get("beeeval_open_case_id"),
                            (oldVal, newVal) -> oldVal // 处理重复键
                    ));

            // 构建同步数据
            List<Map<String, Object>> syncData = new ArrayList<>();

            for (Map<String, Object> testState : testStates) {
                Integer testCaseId = (Integer) testState.get("testcase_id");
                Integer score = (Integer) testState.get("score");

                if (testCaseId == null || score == null) {
                    continue;
                }

                // 从映射中查找对应的开源用例ID
                Integer openCaseId = testCaseToOpenCaseMap.get(testCaseId);

                if (openCaseId != null) {
                    // 查询开源用例的详细信息，获取three_tag_id
                    Long threeTagId = getThreeTagIdByOpenCaseId(openCaseId);

                    Map<String, Object> data = new HashMap<>();
                    data.put("vehicleId", vehicleBaseInfoId);
                    data.put("threeTagId", threeTagId);
                    data.put("caseId", openCaseId);
                    data.put("score", score);

                    syncData.add(data);
                }
            }

            if (CollectionUtils.isEmpty(syncData)) {
                log.warn("没有找到需要同步的开源题目分数数据");
                return Result.ok();
            }

            // 调用BeeEval服务同步数据
            com.xailab.vehicle.feign.common.Result<Void> result = vehicleDataSyncFeign.syncOpenSourceScore(syncData);

            if (result.getCode() == 0) {
                log.info("开源题目分数同步成功，同步了{}条数据", syncData.size());
                return Result.ok();
            } else {
                log.error("开源题目分数同步失败: {}", result.getMsg());
                throw new RuntimeException("开源题目分数同步失败: " + result.getMsg());
            }

        } catch (Exception e) {
            log.error("同步开源题目分数时发生异常", e);
            throw new RuntimeException("同步开源题目分数失败: " + e.getMessage());
        }
    }

    /**
     * 查询功能域分数数据
     */
    private List<DomainScoreSyncVO> queryDomainScoreData(Integer testRecordId, Long vehicleBaseInfoId) {
        List<DomainScoreSyncVO> result = new ArrayList<>();

        try {
            // 查询功能域分数数据（基于TertiaryMetricWeightDao的queryFunctionDomainScore逻辑）
            List<String> recordIds = Collections.singletonList(testRecordId.toString());

            // 这里需要实现具体的查询逻辑，获取功能域的分数数据
            // 由于需要跨数据库查询，我们需要修改查询方式

            // 获取测试记录对应的车辆信息
            TestPlatformVehicleTestRecordEntity testRecord = testPlatformVehicleTestRecordService.getById(testRecordId);
            if (testRecord == null) {
                log.warn("测试记录不存在，testRecordId: {}", testRecordId);
                return result;
            }

            // 调用tertiaryMetricWeightDao.queryTertiaryMetricWeight获取三级指标数据
            List<TertiaryMetricWeightResponse> tertiaryMetrics = tertiaryMetricWeightDao
                    .queryTertiaryMetricWeight(Collections.singletonList(testRecordId.toString()));

            if (CollectionUtils.isEmpty(tertiaryMetrics)) {
                log.warn("没有找到三级指标数据，testRecordId: {}", testRecordId);
                return result;
            }

            // 按功能域分组，计算每个功能域的总分
            Map<String, List<TertiaryMetricWeightResponse>> domainGroup = tertiaryMetrics.stream()
                    .collect(Collectors.groupingBy(TertiaryMetricWeightResponse::getPlanDetailName));

            for (Map.Entry<String, List<TertiaryMetricWeightResponse>> entry : domainGroup.entrySet()) {
                String domainName = entry.getKey();
                List<TertiaryMetricWeightResponse> domainMetrics = entry.getValue();

                // 计算功能域总分：所有三级指标加权分的总和
                double domainTotalScore = domainMetrics.stream()
                        .mapToDouble(TertiaryMetricWeightResponse::getWeightedScore)
                        .sum();

                // 通过映射表找到BeeEval功能域ID
                Long beeevalDomainId = findBeeEvalDomainIdByName(domainName);

                DomainScoreSyncVO vo = new DomainScoreSyncVO();
                vo.setVehicleBaseInfoId(vehicleBaseInfoId);
                vo.setFunctionalDomainId(beeevalDomainId);
                vo.setFunctionalDomainName(domainName);
                vo.setDomainScore(BigDecimal.valueOf(domainTotalScore));
                vo.setTestRecordId(testRecordId);

                result.add(vo);
            }

            log.info("查询到功能域分数数据，条数: {}", result.size());

        } catch (Exception e) {
            log.error("查询功能域分数数据失败", e);
        }

        return result;
    }

    /**
     * 根据测试记录ID获取测试状态数据
     */
    private List<Map<String, Object>> getTestStatesByRecordId(Integer testRecordId) {
        try {
            // 调用DAO方法获取测试状态数据
            List<Map<String, Object>> testStates = testPlatformVehicleTestStateDao
                    .selectTestStatesByRecordId(testRecordId);

            if (CollectionUtils.isEmpty(testStates)) {
                log.warn("没有找到测试状态数据，testRecordId: {}", testRecordId);
                return new ArrayList<>();
            }

            log.info("成功获取测试状态数据，testRecordId: {}, 条数: {}", testRecordId, testStates.size());
            return testStates;

        } catch (Exception e) {
            log.error("获取测试状态数据失败，testRecordId: {}", testRecordId, e);
            return new ArrayList<>();
        }
    }

    /**
     * 根据开源用例ID获取三级指标ID
     */
    private Long getThreeTagIdByOpenCaseId(Integer openCaseId) {
        try {
            // 通过Feign调用BeeEval服务获取开源用例的详细信息
            // 这里需要调用DataSyncService.getOpenSourceCases()获取所有开源用例信息，
            // 然后找到对应的domain_index_id
            com.xailab.vehicle.feign.common.Result<List<Map<String, Object>>> result = vehicleDataSyncFeign
                    .getOpenSourceCases();

            if (result.getCode() == 0 && result.getData() != null) {
                // 找到对应的开源用例
                for (Map<String, Object> openCase : result.getData()) {
                    Integer caseId = (Integer) openCase.get("id");
                    if (caseId != null && caseId.equals(openCaseId)) {
                        Long domainIndexId = (Long) openCase.get("domain_index_id");
                        return domainIndexId != null ? domainIndexId : 0L;
                    }
                }
            }

            log.warn("未找到开源用例的domain_index_id，openCaseId: {}", openCaseId);
            return 0L;
        } catch (Exception e) {
            log.error("获取三级指标ID失败，openCaseId: {}", openCaseId, e);
            return 0L;
        }
    }

    /**
     * 根据数据管理平台功能域名称查找BeeEval功能域ID
     */
    private Long findBeeEvalDomainIdByName(String testDomainName) {
        try {
            PcafeFunctionDomainMappingEntity mapping = pcafeFunctionDomainMappingDao.selectOne(
                    Wrappers.<PcafeFunctionDomainMappingEntity>lambdaQuery()
                            .eq(PcafeFunctionDomainMappingEntity::getTestFunctionDomainName, testDomainName));

            if (mapping != null && mapping.getBeeevalFunctionDomainId() != null) {
                return mapping.getBeeevalFunctionDomainId();
            }

            log.warn("未找到功能域映射，testDomainName: {}", testDomainName);
            return null;
        } catch (Exception e) {
            log.error("查找BeeEval功能域ID失败，testDomainName: {}", testDomainName, e);
            return null;
        }
    }

    /**
     * 查询三级指标分数数据
     */
    private List<TertiaryMetricScoreSyncVO> queryTertiaryMetricScoreData(Integer testRecordId, Long vehicleBaseInfoId) {
        List<TertiaryMetricScoreSyncVO> result = new ArrayList<>();

        try {
            // 获取测试记录对应的车辆信息
            TestPlatformVehicleTestRecordEntity testRecord = testPlatformVehicleTestRecordService.getById(testRecordId);
            if (testRecord == null) {
                log.warn("测试记录不存在，testRecordId: {}", testRecordId);
                return result;
            }

            // 调用tertiaryMetricWeightDao.queryTertiaryMetricWeight获取三级指标数据
            List<TertiaryMetricWeightResponse> tertiaryMetrics = tertiaryMetricWeightDao
                    .queryTertiaryMetricWeight(Collections.singletonList(testRecordId.toString()));

            if (CollectionUtils.isEmpty(tertiaryMetrics)) {
                log.warn("没有找到三级指标数据，testRecordId: {}", testRecordId);
                return result;
            }

            // 转换为同步VO对象
            for (TertiaryMetricWeightResponse metric : tertiaryMetrics) {
                // 通过pcafe_relevancy_function_domain表找到BeeEval指标ID
                PcafeRelevancyFunctionDomainEntity indexMapping = pcafeRelevancyFunctionDomainDao.selectOne(
                        Wrappers.<PcafeRelevancyFunctionDomainEntity>lambdaQuery()
                                .eq(PcafeRelevancyFunctionDomainEntity::getTestFunctionDomainName,
                                        metric.getPlanDetailName())
                                .eq(PcafeRelevancyFunctionDomainEntity::getTestIndexName, metric.getTertiaryMetric()));

                TertiaryMetricScoreSyncVO vo = new TertiaryMetricScoreSyncVO();
                vo.setVehicleBaseInfoId(vehicleBaseInfoId);
                vo.setFunctionalDomainId(findBeeEvalDomainIdByName(metric.getPlanDetailName()));
                vo.setFunctionalDomainName(metric.getPlanDetailName());
                vo.setDomainIndexId(indexMapping != null ? indexMapping.getBeeevalIndexId() : null);
                vo.setIndexName(metric.getTertiaryMetric());
                vo.setIndexScore(BigDecimal.valueOf(metric.getAvgScore())); // 使用平均得分
                vo.setTestRecordId(testRecordId);

                result.add(vo);
            }

            log.info("查询到三级指标分数数据，条数: {}", result.size());

        } catch (Exception e) {
            log.error("查询三级指标分数数据失败", e);
        }

        return result;
    }

    /**
     * 查询基础能力分数数据
     */
    private List<BasicAbilityScoreSyncVO> queryBasicAbilityScoreData(Integer testRecordId, Long vehicleBaseInfoId) {
        List<BasicAbilityScoreSyncVO> result = new ArrayList<>();

        try {
            // 获取测试记录对应的车辆信息
            TestPlatformVehicleTestRecordEntity testRecord = testPlatformVehicleTestRecordService.getById(testRecordId);
            if (testRecord == null) {
                log.warn("测试记录不存在，testRecordId: {}", testRecordId);
                return result;
            }

            // 调用tertiaryMetricWeightDao.queryBaseWeight获取基础能力数据
            List<BaseFunctionVo> baseScores = tertiaryMetricWeightService
                    .queryTertiaryMetricBaseWeight(testRecordId.toString());

            if (CollectionUtils.isEmpty(baseScores)) {
                log.warn("没有找到基础能力数据，testRecordId: {}", testRecordId);
                return result;
            }

            // 转换为同步VO对象
            for (BaseFunctionVo baseFunctionVo : baseScores) {
                BasicAbilityScoreSyncVO vo = new BasicAbilityScoreSyncVO();
                vo.setVehicleBaseInfoId(vehicleBaseInfoId);
                vo.setIndexName(baseFunctionVo.getFunctionName());
                if (baseFunctionVo.getValue() != null) {
                    vo.setIndexScore(BigDecimal.valueOf(baseFunctionVo.getValue()));
                }
                vo.setTestRecordId(testRecordId);
                result.add(vo);
            }

            log.info("查询到基础能力分数数据，条数: {}", result.size());

        } catch (Exception e) {
            log.error("查询基础能力分数数据失败", e);
        }

        return result;
    }
}
