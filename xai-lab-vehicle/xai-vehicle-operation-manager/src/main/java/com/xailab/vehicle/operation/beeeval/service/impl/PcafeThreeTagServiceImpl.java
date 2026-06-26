package com.xailab.vehicle.operation.beeeval.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xailab.vehicle.feign.vehicledata.VehicleDataFeign;
import com.xailab.vehicle.feign.vehicledata.VehiclePcafeRelevancyFunctionThreeTagFeign;
import com.xailab.vehicle.feign.vehicledata.VehicleTreeFeign;
import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.feign.vo.SynchronizationThreeTagResponse;
import com.xailab.vehicle.operation.beeeval.dao.PcafeThreeTagDao;
import com.xailab.vehicle.operation.beeeval.entity.vo.TestCaseThreeTagVo;
import com.xailab.vehicle.operation.beeeval.service.PcafeThreeTagService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@DS("test_platform")
@RequiredArgsConstructor
@Slf4j
public class PcafeThreeTagServiceImpl implements PcafeThreeTagService {

    private final PcafeThreeTagDao pcafeThreeTagDao;

    @Resource
    VehicleTreeFeign vehicleTreeFeign;

    @Resource
    VehiclePcafeRelevancyFunctionThreeTagFeign vehiclePcafeRelevancyFunctionThreeTagFeign;

    @Override
    public List<TestCaseThreeTagVo> getPcafeThreeTagList() {

        return pcafeThreeTagDao.getPcafeThreeTagList();
    }

    @Override
    public Boolean associationByThreeTagNumber(List<SynchronizationThreeTagResponse> threeTagListSynchronization ) {

        List<TestCaseThreeTagVo> pcafeThreeTagList = getPcafeThreeTagList();

        List<PcafeRelevancyFunctionThreeTagEntity> pcafeRelevancyFunctionThreeTagEntities = mapTags(pcafeThreeTagList, threeTagListSynchronization);

        return vehicleTreeFeign.saveList(pcafeRelevancyFunctionThreeTagEntities);
    }

    @Override
    public List<PcafeRelevancyFunctionThreeTagEntity> queryList() {

        return vehiclePcafeRelevancyFunctionThreeTagFeign.queryList();
    }

    public List<PcafeRelevancyFunctionThreeTagEntity> mapTags(
            List<TestCaseThreeTagVo> voList,
            List<SynchronizationThreeTagResponse> responseList) {

        // 1. 预处理Synchronization数据：创建tagNumber->对象的映射
        Map<String, SynchronizationThreeTagResponse> responseMap = new HashMap<>();
        for (SynchronizationThreeTagResponse response : responseList) {
            // 使用统一大写比较，避免大小写问题
            responseMap.put(response.getTagNumber().toUpperCase(), response);
        }

        // 结果列表
        List<PcafeRelevancyFunctionThreeTagEntity> result = new ArrayList<>();

        // 2. 处理每个TestCaseThreeTagVo
        for (TestCaseThreeTagVo vo : voList) {
            // 拆分taskDetail
            String[] parts = vo.getTaskDetail().split("-", 2); // 分割成2部分
            if (parts.length < 2) continue; // 跳过无效数据

            // 提取tagNumber和tagName
            String sourceTagNumber = parts[0].trim();
            String normalizedTagNumber = sourceTagNumber.toUpperCase();

            // 3. 匹配Synchronization数据
            SynchronizationThreeTagResponse matchedResponse = responseMap.get(normalizedTagNumber);
            if (matchedResponse == null) continue; // 无匹配则跳过

            // 4. 创建关联对象
            try {
                PcafeRelevancyFunctionThreeTagEntity entity = new PcafeRelevancyFunctionThreeTagEntity();

                // 转换类型并设置字段
                entity.setBeeevalThreeNumber(matchedResponse.getTagNumber());
                entity.setPecafeThreeNumber(vo.getTaskDetail()); // 使用原值转换

                result.add(entity);
            } catch (NumberFormatException e) {
                // 处理pecafe_three_id转换失败
                System.err.printf("转换失败: %s 不是有效的整数%n", sourceTagNumber);
            }
        }

        return result;
    }

}