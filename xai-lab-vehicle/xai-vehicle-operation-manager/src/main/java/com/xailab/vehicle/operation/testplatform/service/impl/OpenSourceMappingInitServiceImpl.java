package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.feign.vehicledata.VehicleDataSyncFeign;
import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.operation.testplatform.dao.PcafeFunctionDomainMappingDao;
import com.xailab.vehicle.operation.testplatform.dao.TestCaseOpenSourceMappingDao;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehiclePlanDetailDao;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestCaseDao;
import com.xailab.vehicle.operation.testplatform.entity.PcafeFunctionDomainMappingEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestCaseOpenSourceMappingEntity;
import com.xailab.vehicle.operation.testplatform.service.OpenSourceMappingInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 开源用例关联映射初始化服务实现
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Slf4j
@Service("openSourceMappingInitService")
@DS("test_platform")
public class OpenSourceMappingInitServiceImpl implements OpenSourceMappingInitService {

    @Autowired
    private TestCaseOpenSourceMappingDao testCaseOpenSourceMappingDao;

    @Autowired
    private TestPlatformVehicleTestCaseDao testPlatformVehicleTestCaseDao;

    @Autowired
    private TestPlatformVehiclePlanDetailDao testPlatformVehiclePlanDetailDao;

    @Autowired
    private PcafeFunctionDomainMappingDao pcafeFunctionDomainMappingDao;

    @Autowired
    private VehicleDataSyncFeign vehicleDataSyncFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result initOpenSourceMapping() {
        try {
            log.info("开始初始化测试用例与开源用例的关联关系");

            // 获取数据管理平台的测试用例内容
            List<Map<String, Object>> testCases = getTestCasesFromPlatform();

            // 获取BeeEval的开源用例内容
            List<Map<String, Object>> openCases = getOpenCasesFromBeeEval();

            if (CollectionUtils.isEmpty(testCases) || CollectionUtils.isEmpty(openCases)) {
                log.warn("测试用例或开源用例数据为空，测试用例数量: {}, 开源用例数量: {}",
                        testCases.size(), openCases.size());
                return Result.ok();
            }

            // 将开源用例按功能域+内容组合分组，便于匹配
            Map<String, List<Map<String, Object>>> openCaseMap = openCases.stream()
                    .filter(item -> item.get("test_case_content") != null && item.get("function_domain_id") != null)
                    .collect(Collectors.groupingBy(item -> item.get("function_domain_id").toString() + "_"
                            + item.get("test_case_content").toString()));

            int successCount = 0;
            int skipCount = 0;

            // 遍历测试用例，匹配开源用例
            for (Map<String, Object> testCase : testCases) {
                String content = (String) testCase.get("testcase_content");
                Integer testCaseId = (Integer) testCase.get("id");
                Integer functionId = (Integer) testCase.get("function_id");

                if (!StringUtils.hasText(content) || testCaseId == null || functionId == null) {
                    continue;
                }

                // 先通过功能域映射找到BeeEval的功能域ID
                Long beeevalFunctionDomainId = findBeeEvalFunctionDomainId(functionId);
                if (beeevalFunctionDomainId == null) {
                    log.warn("未找到测试用例功能域的BeeEval映射，testCaseId={}, functionId={}", testCaseId, functionId);
                    continue;
                }

                // 根据功能域+内容组合查找匹配的开源用例
                String matchKey = beeevalFunctionDomainId.toString() + "_" + content;
                List<Map<String, Object>> matchedOpenCases = openCaseMap.get(matchKey);

                if (!CollectionUtils.isEmpty(matchedOpenCases)) {
                    // 取第一个匹配的开源用例
                    Map<String, Object> matchedOpenCase = matchedOpenCases.get(0);
                    Integer openCaseId = (Integer) matchedOpenCase.get("id");

                    // 检查是否已经存在映射关系
                    TestCaseOpenSourceMappingEntity existing = testCaseOpenSourceMappingDao.selectOne(
                            Wrappers.<TestCaseOpenSourceMappingEntity>lambdaQuery()
                                    .eq(TestCaseOpenSourceMappingEntity::getTestCaseId, testCaseId)
                                    .eq(TestCaseOpenSourceMappingEntity::getBeeevalOpenCaseId, openCaseId));

                    if (existing == null) {
                        // 创建新的映射关系
                        TestCaseOpenSourceMappingEntity mapping = new TestCaseOpenSourceMappingEntity();
                        mapping.setTestCaseId(testCaseId);
                        mapping.setBeeevalOpenCaseId(openCaseId);

                        testCaseOpenSourceMappingDao.insert(mapping);
                        successCount++;

                        log.debug("创建映射关系成功: testCaseId={}, openCaseId={}, functionId={}, content={}",
                                testCaseId, openCaseId, functionId, content);
                    } else {
                        skipCount++;
                        log.debug("映射关系已存在，跳过: testCaseId={}, openCaseId={}", testCaseId, openCaseId);
                    }
                } else {
                    log.debug("未找到匹配的开源用例: functionId={}, content={}", functionId, content);
                }
            }

            log.info("初始化完成，成功创建{}条映射关系，跳过{}条已存在的映射",
                    successCount, skipCount);
            return Result.ok();

        } catch (Exception e) {
            log.error("初始化开源用例关联关系失败", e);
            throw new RuntimeException("初始化开源用例关联关系失败: " + e.getMessage());
        }
    }

    /**
     * 获取数据管理平台的测试用例数据
     * 这里需要调用实际的DAO方法获取数据
     */
    private List<Map<String, Object>> getTestCasesFromPlatform() {
        try {
            // 调用DAO方法获取测试用例数据
            List<Map<String, Object>> testCases = testPlatformVehicleTestCaseDao.selectTestCaseForMapping();

            if (CollectionUtils.isEmpty(testCases)) {
                log.warn("从数据管理平台获取的测试用例数据为空");
                return List.of();
            }

            log.info("成功从数据管理平台获取{}条测试用例数据", testCases.size());
            return testCases;

        } catch (Exception e) {
            log.error("获取数据管理平台测试用例数据失败", e);
            return List.of();
        }
    }

    /**
     * 获取BeeEval的开源用例数据
     */
    private List<Map<String, Object>> getOpenCasesFromBeeEval() {
        try {
            // 通过Feign调用BeeEval服务获取开源用例数据
            Result<List<Map<String, Object>>> result = vehicleDataSyncFeign.getOpenSourceCases();

            if (result.getCode() == 0 && result.getData() != null) {
                return result.getData();
            } else {
                log.error("获取BeeEval开源用例数据失败: {}", result.getMsg());
                return List.of();
            }
        } catch (Exception e) {
            log.error("获取BeeEval开源用例数据失败", e);
            return List.of();
        }
    }

    /**
     * 根据数据管理平台的功能域ID找到BeeEval的功能域ID
     */
    private Long findBeeEvalFunctionDomainId(Integer testFunctionId) {
        try {
            // 1. 根据功能域ID从plan_detail表获取功能域名称
            String planDetailName = testPlatformVehiclePlanDetailDao.selectPlanDetailNameById(testFunctionId);
            if (!StringUtils.hasText(planDetailName)) {
                log.warn("未找到功能域名称，testFunctionId: {}", testFunctionId);
                return null;
            }

            // 2. 根据功能域名称从映射表查找BeeEval功能域ID
            PcafeFunctionDomainMappingEntity mapping = pcafeFunctionDomainMappingDao.selectOne(
                    Wrappers.<PcafeFunctionDomainMappingEntity>lambdaQuery()
                            .eq(PcafeFunctionDomainMappingEntity::getTestFunctionDomainName, planDetailName));

            if (mapping != null && mapping.getBeeevalFunctionDomainId() != null) {
                return mapping.getBeeevalFunctionDomainId();
            }

            log.warn("未找到BeeEval功能域映射，planDetailName: {}", planDetailName);
            return null;
        } catch (Exception e) {
            log.error("查找BeeEval功能域ID失败，testFunctionId: {}", testFunctionId, e);
            return null;
        }
    }
}