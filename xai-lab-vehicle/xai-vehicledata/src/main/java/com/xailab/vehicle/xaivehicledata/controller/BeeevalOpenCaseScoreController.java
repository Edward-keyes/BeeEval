package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.feign.vo.VehicleIdAndOpenSourceVo;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionTreeOneAndTwoTagResponse;
import com.xailab.vehicle.xaivehicledata.service.BeeevalOpenCaseScoreService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ware/beeevalOpenCaseScore")
public class BeeevalOpenCaseScoreController {

    @Resource
    BeeevalOpenCaseScoreService beeevalOpenCaseScoreService;

    @PostMapping("/saveCaseScoreByVehicleId")
    public Boolean saveCaseScoreByVehicleId(@RequestBody VehicleIdAndOpenSourceVo vo) {

        return beeevalOpenCaseScoreService.saveCaseScoreByVehicleId(vo.getVehicleId(),vo.getOpenSourceVos());
    }

}