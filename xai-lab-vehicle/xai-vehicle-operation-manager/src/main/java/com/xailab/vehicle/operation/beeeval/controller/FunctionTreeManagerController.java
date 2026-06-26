package com.xailab.vehicle.operation.beeeval.controller;

import com.xailab.vehicle.feign.common.PageResultBee;
import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.feign.pojo.treem.*;
import com.xailab.vehicle.feign.vehicledata.FunctionTreeManagerClient;
import com.xailab.vehicle.framework.operatelog.annotations.OperateLog;
import com.xailab.vehicle.framework.operatelog.enums.OperateTypeEnum;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * beeeval功能树管理
 */
@RestController
@RequestMapping("/functionTree/manager")
@Slf4j
public class FunctionTreeManagerController {
    @Resource
    private FunctionTreeManagerClient functionTreeManagerClient;


    /**
     * 查询功能数list
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/queryFunctionTreeList")
    @PreAuthorize("hasAuthority('beeeval:function_tree_m:page')")
    @OperateLog(type = OperateTypeEnum.GET, module = "Beeeval功能树管理", name = "功能树列表")
    public Result<PageResultBee<FunctionTreeQueryListResponse>> queryFunctionTreeList(@RequestBody @Valid FunctionTreeListPageRequest pageQuery, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.error(bindingResult.getFieldError().getDefaultMessage());
        }
        Result<PageResultBee<FunctionTreeQueryListResponse>> pageResultBeeResult = functionTreeManagerClient.queryFunctionTreeList(pageQuery);
        return pageResultBeeResult;
    }


    /**
     * 功能树相关
     */
    /**
     * 功能树更新
     */
    @PostMapping("/functionTreeUpdate")
    @PreAuthorize("hasAuthority('beeeval:function_tree_m:update')")
    @OperateLog(type = OperateTypeEnum.UPDATE, module = "Beeeval功能树管理", name = "功能树更新")
    public Result<Void> functionTreeUpdate(@RequestBody @Valid FunctionTreeUpdateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return functionTreeManagerClient.functionTreeUpdate(request);
    }


    /**
     * 编辑用例
     */
    @PostMapping("/editFunctionTreeCase")
    @PreAuthorize("hasAuthority('beeeval:function_tree_m:update')")
    @OperateLog(type = OperateTypeEnum.UPDATE, module = "Beeeval功能树管理", name = "功能树用例编辑")
    public Result<Void> editFunctionTreeCase(@RequestBody @Valid FunctionTreeCaseEditRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return functionTreeManagerClient.editFunctionTreeCase(request);
    }


    /**
     * 用例素材相关
     */

    /**
     * 用例素材查询
     */
    @PostMapping("/queryCaseMaterial")
    @PreAuthorize("hasAuthority('beeeval:function_tree_m:query')")
    @OperateLog(type = OperateTypeEnum.GET, module = "Beeeval功能树管理", name = "功能树用例素材查询")
    public Result<List<FunctionTreeCaseMaterialResponse>> queryFunctionTreeCaseMaterial(@RequestBody @Valid FunctionTreeCaseMaterialQueryRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return Result.ok(List.of());
        }
        return functionTreeManagerClient.queryFunctionTreeCaseMaterial(request);
    }

    /**
     * 用例素材上传
     *
     * @param file
     * @param vehicleId
     * @param functionTreeCaseId
     */
    @PostMapping("/materialUpload")
    @PreAuthorize("hasAuthority('beeeval:function_tree_m:update')")
//    @OperateLog(type = OperateTypeEnum.UPDATE, module = "Beeeval功能树管理", name = "功能树用例素材上传")
    public Result<Void> functionTreeCaseMaterialUpload(@RequestPart(name = "file") MultipartFile file, @RequestParam(name = "vehicleId") Long vehicleId,
                                                       @RequestParam(name = "functionTreeCaseId") Long functionTreeCaseId) {
        return functionTreeManagerClient.functionTreeCaseMaterialUpload(file, vehicleId, functionTreeCaseId);
    }

    /**
     * 用例素材删除
     *
     * @param id
     */
    @PostMapping("/deleteCaseMaterial")
    @PreAuthorize("hasAuthority('beeeval:function_tree_m:delete')")
    @OperateLog(type = OperateTypeEnum.DELETE, module = "Beeeval功能树管理", name = "功能树用例素材删除")
    public Result<Void> deleteFunctionTreeCaseMaterial(@RequestParam("id") Long id) {
        return functionTreeManagerClient.deleteFunctionTreeCaseMaterial(id);
    }

}
