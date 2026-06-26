package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.feign.common.PageResultBee;
import com.xailab.vehicle.feign.pojo.treem.*;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.service.FunctionTreeManagerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/functionTree/manager")
public class FunctionTreeManagerController {

    @Resource
    private FunctionTreeManagerService functionTreeManagerService;


    /**
     * 查询功能数list
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/queryFunctionTreeList")
    public Result<PageResultBee<FunctionTreeQueryListResponse>> queryFunctionTreeList(@RequestBody @Valid FunctionTreeListPageRequest pageQuery, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.ok(functionTreeManagerService.queryFunctionTreeList(pageQuery));
    }


    /**
     * 功能树相关
     */
    /**
     * 功能树更新
     */
    @PostMapping("/functionTreeUpdate")
    public Result<Void> functionTreeUpdate(@RequestBody @Valid FunctionTreeUpdateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return functionTreeManagerService.functionTreeUpdate(request);
    }


    /**
     * 编辑用例
     */
    @PostMapping("/editFunctionTreeCase")
    public Result<Void> editFunctionTreeCase(@RequestBody @Valid FunctionTreeCaseEditRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return functionTreeManagerService.editFunctionTreeCase(request);
    }


    /**
     * 用例素材相关
     */

    /**
     * 用例素材查询
     */
    @PostMapping("/queryCaseMaterial")
    public Result<List<FunctionTreeCaseMaterialResponse>> queryFunctionTreeCaseMaterial(@RequestBody @Valid FunctionTreeCaseMaterialQueryRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.ok(List.of());
        }
        return Result.ok(functionTreeManagerService.queryFunctionTreeCaseMaterial(request));
    }

    /**
     * 用例素材上传
     *
     * @param file
     * @param vehicleId
     * @param functionTreeCaseId
     */
    @PostMapping("/materialUpload")
    public Result<Void> functionTreeCaseMaterialUpload(@RequestPart(name = "file") MultipartFile file,@RequestParam(name = "vehicleId") Long vehicleId,
                                                       @RequestParam(name = "functionTreeCaseId") Long functionTreeCaseId) {
        return functionTreeManagerService.functionTreeCaseMaterialUpload(file, vehicleId, functionTreeCaseId);
    }

    /**
     * 用例素材删除
     *
     * @param id
     */
    @PostMapping("/deleteCaseMaterial")
    public Result<Void> deleteFunctionTreeCaseMaterial(@RequestParam("id") Long id) {
        return functionTreeManagerService.deleteFunctionTreeCaseMaterial(id);
    }


}
