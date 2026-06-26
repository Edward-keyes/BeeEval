package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserSuggestionEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleUserSuggestionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehiclesuggestion")
public class VehicleUserSuggestionController {

    @Resource
    private VehicleUserSuggestionService vehicleUserSuggestionService;

    /**
     * 保存用户反馈
     */
    @PostMapping("/save")
    public Result saveSuggestion(@RequestBody VehicleUserSuggestionEntity suggestion) {
        return vehicleUserSuggestionService.saveSuggestion(suggestion);
    }

    /**
     * 获取用户反馈列表
     */
    @GetMapping("/list")
    public Result getSuggestionList() {
        return vehicleUserSuggestionService.getSuggestionList();
    }
}