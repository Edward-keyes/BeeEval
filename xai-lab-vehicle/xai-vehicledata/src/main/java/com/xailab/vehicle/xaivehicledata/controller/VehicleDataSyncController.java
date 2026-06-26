package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaivehicledata.common.ApiResponse;
import com.xailab.vehicle.xaivehicledata.service.VehicleDataSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆数据同步控制器
 */
@Slf4j
@Tag(name = "车辆数据同步接口")
@RestController
@RequestMapping("/api/v1/qa/vehicle-sync")
@RequiredArgsConstructor
public class VehicleDataSyncController {

    private final VehicleDataSyncService vehicleDataSyncService;

    @Operation(summary = "同步所有车辆数据到知识库")
    @PostMapping("/sync-all")
    public ApiResponse<String> syncAllVehicles() {
        try {
            int count = vehicleDataSyncService.syncAllVehiclesToKnowledgeBase();
            return ApiResponse.success("成功同步" + count + "条车辆数据");
        } catch (Exception e) {
            log.error("同步车辆数据失败", e);
            return ApiResponse.error("同步失败: " + e.getMessage());
        }
    }

    @Operation(summary = "同步指定车辆数据到知识库")
    @PostMapping("/sync")
    public ApiResponse<String> syncVehicles(@RequestBody List<Long> vehicleIds) {
        try {
            int count = vehicleDataSyncService.syncVehiclesToKnowledgeBase(vehicleIds);
            return ApiResponse.success("成功同步" + count + "条车辆数据");
        } catch (Exception e) {
            log.error("同步车辆数据失败", e);
            return ApiResponse.error("同步失败: " + e.getMessage());
        }
    }

    @Operation(summary = "清空知识库中的车辆数据")
    @DeleteMapping("/clear")
    public ApiResponse<String> clearVehicleData() {
        try {
            int count = vehicleDataSyncService.clearVehicleDataFromKnowledgeBase();
            return ApiResponse.success("成功删除" + count + "条车辆数据");
        } catch (Exception e) {
            log.error("清空车辆数据失败", e);
            return ApiResponse.error("清空失败: " + e.getMessage());
        }
    }

    @Operation(summary = "生成车辆数据INSERT SQL语句")
    @GetMapping("/generate-sql")
    public ApiResponse<String> generateInsertSql() {
        try {
            String sql = vehicleDataSyncService.generateVehicleDataInsertSql();
            return ApiResponse.success(sql);
        } catch (Exception e) {
            log.error("生成INSERT SQL失败", e);
            return ApiResponse.error("生成失败: " + e.getMessage());
        }
    }
}
