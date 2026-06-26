package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.ExcelUtils;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.dao.BeeevalOpenCaseScoreDao;
import com.xailab.vehicle.xaivehicledata.dao.BeeevalOpenSourceCaseDao;
import com.xailab.vehicle.xaivehicledata.dao.VehicleTestResultAllDao;
import com.xailab.vehicle.xaivehicledata.entity.BaseInfoEntity;
import com.xailab.vehicle.xaivehicledata.entity.BeeevalOpenCaseScoreEntity;
import com.xailab.vehicle.xaivehicledata.entity.BeeevalOpenSourceCaseEntity;
import com.xailab.vehicle.xaivehicledata.entity.VehicleTestResultAllEntity;
import com.xailab.vehicle.xaivehicledata.entity.response.TestResultResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.Problem;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleInfoVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.VehicleTestResultVo;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import com.xailab.vehicle.xaivehicledata.service.DomainIndexService;
import com.xailab.vehicle.xaivehicledata.service.VehicleTestResultAllService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service("vehicleTestResultAllService")
@Slf4j
public class VehicleTestResultAllServiceImpl extends ServiceImpl<VehicleTestResultAllDao, VehicleTestResultAllEntity>
        implements VehicleTestResultAllService {

    private static final SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(0, 0);

    @Autowired
    BaseInfoService baseInfoService;

    @Autowired
    DomainIndexService domainIndexService;

    @Resource
    VehicleTestResultAllDao vehicleTestResultAllDao;

    @Resource
    BeeevalOpenCaseScoreDao beeevalOpenCaseScoreDao;

    @Resource
    BeeevalOpenSourceCaseDao beeevalOpenSourceCaseDao;


    @Override
    public Result saveTestResult(VehicleTestResultAllEntity testResult) {
        try {
            testResult.setId(snowflakeIdGenerator.nextId());
            save(testResult);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("保存测试结果失败：" + e.getMessage());
        }
    }

    @Override
    public Result getTestResultList() {
        try {
            QueryWrapper<VehicleTestResultAllEntity> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("id");
            List<VehicleTestResultAllEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取测试结果列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result getTestResultListByVehicleId(Long vehicleId) {
        try {
            QueryWrapper<VehicleTestResultAllEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("vehicle_id", vehicleId)
                    .orderByDesc("id");
            List<VehicleTestResultAllEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取车辆测试结果列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result getTestResultListByDomainFunctionId(Long domainFunctionId) {
        try {
            QueryWrapper<VehicleTestResultAllEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("domain_function_id", domainFunctionId)
                    .orderByDesc("id");
            List<VehicleTestResultAllEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取功能域测试结果列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result getTestResultListByFunctionIndexId(Long functionIndexId) {
        try {
            QueryWrapper<VehicleTestResultAllEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("function_index_id", functionIndexId)
                    .orderByDesc("id");
            List<VehicleTestResultAllEntity> list = list(wrapper);
            return Result.ok(list);
        } catch (Exception e) {
            return Result.error("获取三级指标测试结果列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<TestResultResponse>> getTestResultListByMultipleIds(List<String> vehicleId, String functionIndexId,String language) {
        Map<String, String> brandVehicleVersionMap = new HashMap<>();
        if (language.equals("en")) {
            brandVehicleVersionMap=baseInfoService.getBrandVehicleVersionEnMap2();
        }else {
            brandVehicleVersionMap=baseInfoService.getBrandVehicleVersionMap2();
        }

        try {
            QueryWrapper<VehicleTestResultAllEntity> wrapper = new QueryWrapper<>();

            // 添加查询条件，只有当参数不为null时才添加条件
            if (vehicleId != null) {
                wrapper.in("vehicle_id", vehicleId);
            }
            if (functionIndexId != null) {
                wrapper.eq("function_index_id", functionIndexId);
            }

            // 按ID降序排序
            wrapper.orderByDesc("id");

            List<VehicleTestResultAllEntity> list = list(wrapper);
            if(language.equals("en")) {
                for (VehicleTestResultAllEntity a : list) {
                    a.setExample(a.getExampleEn());
                }
            }
            List<TestResultResponse> transform = transform(list,brandVehicleVersionMap,language);

            // 获取车辆信息映射
            Map<String, VehicleInfoVo> vehicleInfoMap = baseInfoService.queryAllVehicle(language).stream()
                    .collect(Collectors.toMap(VehicleInfoVo::getId, u -> u));

            // 为请求的车辆添加同步的开源题目分值数据
            for (String vehicleIdStr : vehicleId) {
                Long vehicleIdLong = Long.parseLong(vehicleIdStr);
                Long functionIndexIdLong = Long.parseLong(functionIndexId);

                // 查询该车辆在指定指标下的所有开源题目分值
                List<BeeevalOpenCaseScoreEntity> syncScores = beeevalOpenCaseScoreDao.selectList(
                    Wrappers.<BeeevalOpenCaseScoreEntity>lambdaQuery()
                        .eq(BeeevalOpenCaseScoreEntity::getVehicleId, vehicleIdLong)
                        .eq(BeeevalOpenCaseScoreEntity::getThreeTagId, functionIndexIdLong)
                );

                if (!CollectionUtils.isEmpty(syncScores)) {
                    // 检查是否已经存在该车辆的结果，避免重复添加
                    boolean alreadyExists = transform.stream()
                        .anyMatch(t -> t.getVehicleId().equals(vehicleIdStr));

                    if (!alreadyExists) {
                        TestResultResponse testResult = new TestResultResponse();
                        testResult.setVehicleId(vehicleIdStr);

                        // 设置车辆名称
                        VehicleInfoVo vehicleInfo = vehicleInfoMap.get(vehicleIdStr);
                        if (vehicleInfo != null) {
                            testResult.setVehicleName(vehicleInfo.getBrandModel());
                        } else {
                            testResult.setVehicleName("未知车辆");
                        }

                        // 转换开源题目分值为Problem列表
                        List<Problem> problemList = new ArrayList<>();
                        for (BeeevalOpenCaseScoreEntity syncScore : syncScores) {
                            // 根据case_id查询开源题目内容
                            BeeevalOpenSourceCaseEntity openCase = beeevalOpenSourceCaseDao.selectById(syncScore.getCaseId());
                            if (openCase != null) {
                                Problem problem = new Problem();
                                problem.setScore(syncScore.getScore());
                                problem.setQuestion(openCase.getTestCaseContent());
                                problemList.add(problem);
                            }
                        }

                        testResult.setProblemList(problemList);
                        transform.add(testResult);

                        log.debug("为车辆{}添加同步的开源题目分值数据，共{}条", vehicleIdStr, problemList.size());
                    }
                }
            }
            return Result.ok(transform);
        } catch (Exception e) {
            return Result.error("获取测试结果列表失败：" + e.getMessage());
        }
    }

    public static List<TestResultResponse> transform(List<VehicleTestResultAllEntity> vehicleTestResultAllEntities, Map<String, String> brandVehicleVersionMap, String language) {
        // 使用Map来按vehicleId分组
        Map<String, TestResultResponse> resultMap = new HashMap<>();

        for (VehicleTestResultAllEntity entity : vehicleTestResultAllEntities) {
            String vehicleId = String.valueOf(entity.getVehicleId());
            // 如果已经存在对应的TestResultResponse，则直接获取
            TestResultResponse response = resultMap.getOrDefault(vehicleId, new TestResultResponse());
            response.setVehicleId(vehicleId); // 设置vehicleId
            String s = brandVehicleVersionMap.get(vehicleId);
            response.setVehicleName(s.substring(0,s.lastIndexOf("-"))); // 设置vehicleName

            // 创建Problem对象
            Problem problem = new Problem();
            problem.setScore(entity.getScore()); // 设置score
            problem.setQuestion(entity.getExample()); // 设置question

            // 如果problemList为空，则初始化
            if (response.getProblemList() == null) {
                response.setProblemList(new ArrayList<>());
            }
            // 将Problem对象添加到problemList中
            response.getProblemList().add(problem);

            // 将更新后的TestResultResponse存回map
            resultMap.put(vehicleId, response);
        }

        // 将Map中的values转换为List
        return new ArrayList<>(resultMap.values());
    }

    @Override
    public void input(MultipartFile file) {

        List<VehicleTestResultVo> vos = null;

        Map<String, String> brandVehicleVersionMap = baseInfoService.getBrandVehicleVersionMap();

        Map<String, String> domainIndexMap = domainIndexService.getDomainIndexMap();

        try {
            vos = ExcelUtils.readMultipartFile(file, VehicleTestResultVo.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        List<VehicleTestResultAllEntity> list = new ArrayList<>();

        for (VehicleTestResultVo vo : vos) {
            VehicleTestResultAllEntity vehicleTestResultAllEntity = new VehicleTestResultAllEntity();

            vehicleTestResultAllEntity.setId(snowflakeIdGenerator.nextId());

            vehicleTestResultAllEntity.setVehicleId(Long.valueOf(brandVehicleVersionMap.get(vo.getVehicleModel())));

            vehicleTestResultAllEntity.setScore(Integer.valueOf(vo.getScore()));

            vehicleTestResultAllEntity.setExample(vo.getCaseName());

            vehicleTestResultAllEntity.setExampleEn(vo.getCaseNameEn());

            String s = domainIndexMap.get(vo.getFunctionDomain() + "-" + vo.getFunctionIndex());

            if (Objects.nonNull(s)) {
                vehicleTestResultAllEntity.setFunctionIndexId(Long.valueOf(s));

                list.add(vehicleTestResultAllEntity);
            }

        }
        System.out.println("装填完成");
        saveBatch(list);
    }
}