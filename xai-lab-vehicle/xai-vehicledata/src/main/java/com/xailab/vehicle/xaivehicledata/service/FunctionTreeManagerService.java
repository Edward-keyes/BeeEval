package com.xailab.vehicle.xaivehicledata.service;

import com.xailab.vehicle.feign.common.PageResultBee;
import com.xailab.vehicle.feign.pojo.treem.*;
import com.xailab.vehicle.xaicommon.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FunctionTreeManagerService {


    /**
     * 查询功能数list
     * @param pageQuery
     * @return
     */
    PageResultBee<FunctionTreeQueryListResponse> queryFunctionTreeList(FunctionTreeListPageRequest pageQuery);


    /**
     * 功能树相关
     */
    /**
     * 功能树更新
     */
    Result<Void> functionTreeUpdate(FunctionTreeUpdateRequest request);


//    /**
//     * 添加功能树
//     * 暂定
//     */
//    void functionTreeAdd(FunctionTreeAddRequest request);




    /**
     * 用例相关
     */

//    /**
//     * 添加用例
//     */
//    void addFunctionTreeCase(FunctionTreeCaseAddRequest request);

    /**
     * 编辑用例
     */
    Result<Void> editFunctionTreeCase(FunctionTreeCaseEditRequest request);


    /**
     * 用例素材相关
     */

    /**
     * 用例素材查询
     */
    List<FunctionTreeCaseMaterialResponse> queryFunctionTreeCaseMaterial(FunctionTreeCaseMaterialQueryRequest request);

    /**
     * 用例素材上传
     * @param file
     * @param vehicleId
     * @param functionTreeCaseId
     */
    Result<Void> functionTreeCaseMaterialUpload(MultipartFile file,Long vehicleId,Long functionTreeCaseId);

    /**
     * 用例素材删除
     * @param id
     */
    Result<Void> deleteFunctionTreeCaseMaterial(Long id);







}
