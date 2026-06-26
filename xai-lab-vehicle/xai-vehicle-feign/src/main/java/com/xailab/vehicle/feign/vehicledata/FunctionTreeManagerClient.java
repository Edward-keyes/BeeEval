package com.xailab.vehicle.feign.vehicledata;


import com.xailab.vehicle.feign.ServerNames;
import com.xailab.vehicle.feign.common.PageResultBee;
import com.xailab.vehicle.feign.common.Result;
import com.xailab.vehicle.feign.pojo.treem.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = ServerNames.SYSTEM_SERVER_NAME, path = "/xai-vehicledata/functionTree/manager", contextId = "functionTreeManagerClient")
public interface FunctionTreeManagerClient {

    /**
     * 查询功能数list
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/queryFunctionTreeList")
    Result<PageResultBee<FunctionTreeQueryListResponse>> queryFunctionTreeList(@RequestBody @Valid FunctionTreeListPageRequest pageQuery);


    /**
     * 功能树相关
     */
    /**
     * 功能树更新
     */
    @PostMapping("/functionTreeUpdate")
    Result<Void> functionTreeUpdate(@RequestBody @Valid FunctionTreeUpdateRequest request);


    /**
     * 编辑用例
     */
    @PostMapping("/editFunctionTreeCase")
    Result<Void> editFunctionTreeCase(@RequestBody @Valid FunctionTreeCaseEditRequest request);


    /**
     * 用例素材相关
     */

    /**
     * 用例素材查询
     */
    @PostMapping("/queryCaseMaterial")
    Result<List<FunctionTreeCaseMaterialResponse>> queryFunctionTreeCaseMaterial(@RequestBody @Valid FunctionTreeCaseMaterialQueryRequest request);

    /**
     * 用例素材上传
     *
     * @param file
     * @param vehicleId
     * @param functionTreeCaseId
     */
    @PostMapping(path="/materialUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<Void> functionTreeCaseMaterialUpload(@RequestPart(name = "file") MultipartFile file, @RequestParam(name = "vehicleId") Long vehicleId,
                                                @RequestParam(name = "functionTreeCaseId") Long functionTreeCaseId);

    /**
     * 用例素材删除
     *
     * @param id
     */
    @PostMapping("/deleteCaseMaterial")
    Result<Void> deleteFunctionTreeCaseMaterial(@RequestParam("id") Long id);

}
