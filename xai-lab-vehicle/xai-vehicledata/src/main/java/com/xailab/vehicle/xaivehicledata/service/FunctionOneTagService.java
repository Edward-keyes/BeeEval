package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaivehicledata.entity.FunctionOneTagEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.*;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionTreeOneAndTwoTagResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionOneTagVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionThreeTagVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionTreeVideoNewVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalDomainVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
public interface FunctionOneTagService extends IService<FunctionOneTagEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void inputFunctionTree(MultipartFile file);

    void functionTreeData(String path);

    void functionDomainData(String path);

    void functionDomainNewData(String path);

    List<FunctionDomainResultVo> queryAllOneTagCountThreeTag(String language);

    List<HighlightFunctionRequest> queryAllHighlightFunction(String language);

    List<FunctionOneTagVo> queryAllFunctionTagTree(String language);

    FunctionTreeVideoRequest queryVideoByThreeTagIdAndVehicleId(QueryFunctionTreeVideoRequest request);

    List<FunctionTreeCompareRequest> queryOtherVideoByThreeTagId(String threeTagId, String vehicleId,String language);

    List<FunctionTreeOneAndTwoTagResponse> getAllOneAndTwoTag();

    FunctionTreeVideoNewRequest queryVideoByThreeTagIdAndVehicleIdNew(QueryFunctionTreeVideoRequest functionTreeVideoRequest);

    FunctionTreeVideoNewRequest convert(List<FunctionTreeVideoNewVo> voList);
}

