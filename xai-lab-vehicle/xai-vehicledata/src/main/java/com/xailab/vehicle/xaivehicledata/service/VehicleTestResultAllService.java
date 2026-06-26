package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleTestResultAllEntity;
import com.xailab.vehicle.xaivehicledata.entity.response.TestResultResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleTestResultAllService extends IService<VehicleTestResultAllEntity> {
    /**
     * 保存测试结果
     */
    Result saveTestResult(VehicleTestResultAllEntity testResult);

    /**
     * 获取测试结果列表
     */
    Result getTestResultList();

    /**
     * 根据车辆ID获取测试结果列表
     */
    Result getTestResultListByVehicleId(Long vehicleId);

    /**
     * 根据功能域ID获取测试结果列表
     */
    Result getTestResultListByDomainFunctionId(Long domainFunctionId);

    /**
     * 根据三级指标ID获取测试结果列表
     */
    Result getTestResultListByFunctionIndexId(Long functionIndexId);

    /**
     * 根据车辆ID、功能域ID和三级指标ID获取测试结果列表
     * 
     * @param vehicleId        车辆ID
     * @param functionIndexId  三级指标ID
     * @return 测试结果列表
     */
    Result<List<TestResultResponse>> getTestResultListByMultipleIds(List<String> vehicleId, String functionIndexId,String language);

    void input(MultipartFile file);
}