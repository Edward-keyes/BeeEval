package com.xailab.vehicle.operation.testplatform.controller;

import com.xailab.vehicle.framework.common.utils.Result;
import com.xailab.vehicle.operation.testplatform.pojo.request.PerceptionTreeEditRequest;
import com.xailab.vehicle.operation.testplatform.pojo.request.PerceptionTreeRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.PerceptionTreeResponse;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreePerceptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("testplatform/function_tree_perception")
@Tag(name = "感知功能树")
public class FunctionTreePerceptionController {

    @Autowired
    public FunctionTreePerceptionService functionTreePerceptionService;

    @PostMapping("queryPerceptionTree")
    @PreAuthorize("hasAuthority('testplatform:function_tree_perception:page')")
    public Result<List<PerceptionTreeResponse>> queryPerceptionTree(@RequestBody PerceptionTreeRequest request) {

        List<PerceptionTreeResponse> responses =functionTreePerceptionService.queryPerceptionTree(request.getRecordId());

        return Result.ok(responses);
    }

    @PostMapping("editPerceptionTree")
    @PreAuthorize("hasAuthority('testplatform:function_tree_perception:updateList')")
    public Result editPerceptionTree(@RequestBody PerceptionTreeEditRequest request) {

        Boolean result = functionTreePerceptionService.editPerceptionTree(request.getRecordId(),request.getPerceptionTreeEditVoList());

        return Result.ok(result);
    }
}
