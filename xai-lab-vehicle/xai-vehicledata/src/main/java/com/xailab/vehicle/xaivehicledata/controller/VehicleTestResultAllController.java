package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaicommon.utils.R;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleTestResultAllEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.TestResultListByMultipleIds;
import com.xailab.vehicle.xaivehicledata.entity.response.TestResultResponse;
import com.xailab.vehicle.xaivehicledata.service.VehicleTestResultAllService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/vehicletestresult")
public class VehicleTestResultAllController {

    @Resource
    private VehicleTestResultAllService vehicleTestResultAllService;

    /**
     * 保存测试结果
     */
    @PostMapping("/save")
    public Result saveTestResult(@RequestBody VehicleTestResultAllEntity testResult) {
        return vehicleTestResultAllService.saveTestResult(testResult);
    }

    /**
     * 获取测试结果列表
     */
    @GetMapping("/list")
    public Result getTestResultList() {
        return vehicleTestResultAllService.getTestResultList();
    }

    /**
     * 根据车辆ID获取测试结果列表
     */
    @GetMapping("/list/vehicle/{vehicleId}")
    public Result getTestResultListByVehicleId(@PathVariable("vehicleId") Long vehicleId) {
        return vehicleTestResultAllService.getTestResultListByVehicleId(vehicleId);
    }

    /**
     * 根据功能域ID获取测试结果列表
     */
    @GetMapping("/list/domain/{domainFunctionId}")
    public Result getTestResultListByDomainFunctionId(@PathVariable("domainFunctionId") Long domainFunctionId) {
        return vehicleTestResultAllService.getTestResultListByDomainFunctionId(domainFunctionId);
    }

    /**
     * 根据三级指标ID获取测试结果列表
     */
    @GetMapping("/list/index/{functionIndexId}")
    public Result getTestResultListByFunctionIndexId(@PathVariable("functionIndexId") Long functionIndexId) {
        return vehicleTestResultAllService.getTestResultListByFunctionIndexId(functionIndexId);
    }

    /**
     * 根据车辆ID、功能域ID和三级指标ID获取测试结果列表
     * @return 测试结果列表
     */
    @PostMapping("/list/multiple")
    public Result<List<TestResultResponse>> getTestResultListByMultipleIds(
            @RequestBody TestResultListByMultipleIds request) {
        return vehicleTestResultAllService.getTestResultListByMultipleIds(request.getVehicleId(), request.getFunctionIndexId(),request.getLanguage());
    }

    /**
     * 导入测试数据
     */
    @RequestMapping(value = "/import",consumes = "multipart/form-data")
    public Result input(@RequestPart("file") MultipartFile file) throws Exception {

        vehicleTestResultAllService.input(file);

        return null;
    }
}