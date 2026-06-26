package com.xailab.vehicle.operation.beeeval.controller;

import com.xailab.vehicle.feign.vehicledata.VehicleTreeFeign;
import com.xailab.vehicle.feign.vo.PcafeRelevancyFunctionThreeTagEntity;
import com.xailab.vehicle.feign.vo.SynchronizationThreeTagResponse;
import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.beeeval.service.PcafeThreeTagService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/threeTag")
@AllArgsConstructor
public class FunctionThreeTagController {

    @Resource
    VehicleTreeFeign vehicleTreeFeign;

    private PcafeThreeTagService pcafeThreeTagService;

    /**
     * 基于三级标签编号设置默认结构关联表数据
     */
    @PostMapping("associationByThreeTagNumber")
    @PreAuthorize("hasAnyAuthority('beeeval:function_three_tag:associationByThreeTagNumber')")
    public Result<String> associationByThreeTagNumber(){

        List<SynchronizationThreeTagResponse> threeTagListSynchronization = vehicleTreeFeign.getThreeTagListSynchronization();

        Boolean result = pcafeThreeTagService.associationByThreeTagNumber(threeTagListSynchronization);

        if (result) {

            return Result.ok("关联成功");

        }else {

            return Result.error("关联失败");

        }

    }

    /**
     * 查询默认结构关联
     */
    @PostMapping("queryDefaultStructureAssociation")
    @PreAuthorize("hasAnyAuthority('beeeval:function_three_tag:queryDefaultStructureAssociation')")
    public Result<List<PcafeRelevancyFunctionThreeTagEntity>> queryDefaultStructureAssociation(){

        List<PcafeRelevancyFunctionThreeTagEntity> list = pcafeThreeTagService.queryList();

        return Result.ok(list);
    }

}
