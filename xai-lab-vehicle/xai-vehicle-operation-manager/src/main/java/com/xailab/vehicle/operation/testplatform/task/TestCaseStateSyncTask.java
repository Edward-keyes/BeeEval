package com.xailab.vehicle.operation.testplatform.task;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.vehicle.operation.testplatform.config.TestCaseStateConfig;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestCaseDao;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestStateDao;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeResultMeterialEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName: TestCaseStateSyncTask
 * @Description:
 * @author: liulin
 * @date: 2025/7/20 16:35
 */
@Component
@Slf4j
@DS("test_platform")
public class TestCaseStateSyncTask {

    @Resource
    public TestPlatformVehicleTestStateDao testPlatformVehicleTestStateDao;

    @Resource
    public TestPlatformVehicleTestCaseDao testPlatformVehicleTestCaseDao;

    @Autowired
    public TestCaseStateConfig testCaseStateConfig;

    @Value("${test_platform.function-tree.defaultScenarioId:6}")
    private Integer defaultScenarioId;
    @Value("${test_platform.function-tree.defaultScenarioTask:功能走查}")
    private String defaultScenarioTask;


    /**
     * 每小时同步测试用例状态
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void syncTestCaseState() {
        log.info("开始同步测试用例状态");
        if (!testCaseStateConfig.getTaskEnable()) {
            log.info("测试用例状态同步任务已关闭");
            return;
        }
        List<Integer> caseIds = testPlatformVehicleTestCaseDao.selectIdByScenarioId(defaultScenarioId);
        log.info("需要同步的测试用例id为：{}",caseIds);
        if (CollectionUtils.isEmpty(caseIds)){
            log.info("没有需要同步的测试用例");
            return;
        }
        //查询测试用例状态
        for (Integer caseId : caseIds) {
            List<TestPlatformVehicleTestStateEntity> stateEntities = testPlatformVehicleTestStateDao.selectList(Wrappers.<TestPlatformVehicleTestStateEntity>lambdaQuery()
                    .eq(TestPlatformVehicleTestStateEntity::getTestcaseId, caseId)
                    .and(j ->
                            j.eq(TestPlatformVehicleTestStateEntity::getMaterialState, FunctionTreeResultMeterialEnum.NO_TEST.getValue()).or().isNull(TestPlatformVehicleTestStateEntity::getMaterialState))
            );
            if (CollectionUtils.isEmpty(stateEntities)){
                log.info("没有需要同步的测试用例状态");
                continue;
            }
            log.info("需要同步的测试用例状态为：{}",stateEntities);
            List<TestPlatformVehicleTestStateEntity> list = stateEntities.stream().map(it -> {
                //判断现有给定状态
                FunctionTreeResultMeterialEnum updateState = null;
                if (Objects.nonNull(it.getTestStatus()) && (it.getTestStatus().equals(1) || Objects.nonNull(it.getErrorType())) ) {
                    if (it.getIsSuccessful().equals(1) && Objects.isNull(it.getErrorType())) {
                        updateState = FunctionTreeResultMeterialEnum.NA;
                    } else {
                        updateState=FunctionTreeResultMeterialEnum.NOT_AVAILABLE;
                    }
                } else {
                    updateState = FunctionTreeResultMeterialEnum.NO_TEST;
                }
                log.info("同步的测试用例状态为：testStateId:{}，state:{}", it.getId(), updateState.getValue());
                if (updateState.getValue().equals(it.getMaterialState())){
                    return null;
                }
                it.setMaterialState(updateState.getValue());
                return it;
            }).filter(Objects::nonNull).toList();
            if (CollectionUtils.isNotEmpty(list)){
                log.info("同步编辑的测试用例状态为：{}",list);
                testPlatformVehicleTestStateDao.insertOrUpdate(list);
            }
        }
        log.info("同步测试用例状态结束");
    }


}
