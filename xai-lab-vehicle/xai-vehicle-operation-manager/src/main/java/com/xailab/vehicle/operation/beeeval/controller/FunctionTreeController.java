package com.xailab.vehicle.operation.beeeval.controller;

import com.xailab.vehicle.feign.vehicledata.VehicleDataFeign;
import com.xailab.vehicle.feign.vo.FunctionTreeOpResponse;
import com.xailab.vehicle.feign.vo.SortRequest;
import com.xailab.vehicle.framework.common.utils.Result;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/functionTree")
@AllArgsConstructor
public class FunctionTreeController {

    @Resource
    VehicleDataFeign vehicleDataFeign;

    @PostMapping("/getFunctionTagList")
    @PreAuthorize("hasAuthority('beeeval:function_tree:page')")
    public Result<List<FunctionTreeOpResponse>> getFunctionTagList() {

        return Result.ok(vehicleDataFeign.getFunctionTagList());
    }

    @PostMapping("/sortThreeTag")
    @PreAuthorize("hasAuthority('beeeval:function_tree:sort')")
    public Result<String> sortThreeTag(@RequestBody SortRequest sortRequest) {
        Integer i = vehicleDataFeign.sortThreeTag(sortRequest);
        if (i == 200) {
            return Result.ok("排序成功！！");
        }else{
            return Result.error("排序失败，请重试！！");
        }

    }

}
