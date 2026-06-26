package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncRequest;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeTaskSyncJournalResponse;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.FunctionTreeEntity;
import com.xailab.vehicle.xaivehicledata.entity.ThreeTagList;
import com.xailab.vehicle.xaivehicledata.entity.request.PerceptionAbilityRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.FileUrlResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.FirstLevelTagRatioResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionRichnessRatioRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.VehicleFunctionGradeResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 *
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
public interface FunctionTreeService extends IService<FunctionTreeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ThreeTagList> queryPenetrationRateByFunctionTreeId(List<String> ids,Long allVehicleCount,Boolean isTry,String language,Long loginId);

    List<FirstLevelTagRatioResponse> queryFirstLevelTagRatioByVehicleIds(List<String> ids,String language);

    List<VehicleFunctionGradeResponse> queryVehicleFunctionGradeByFunctionTreeIdsAndVehicleIds(List<String> oneTagIds, List<String> vehicleIds,String language);

    FileUrlResponse queryVideoOrPictureByThreeTagIdAndVehicleId(String threeTagIds, String vehicleIds,String language);

    List<FunctionRichnessRatioRequest> queryFunctionRichnessRatioByVehicleId(String id,String language);

    List<PerceptionAbilityRequest> queryPerceptionAbilityByVehicleId(List<String> id,String language);

    /**
     * 数据同步
     * @param request
     * @return
     */
    Result<FunctionTreeTaskSyncJournalResponse> pcafeDataSync(FunctionTreeDataSyncRequest request);

    /**
     * 数据同步回滚
     * @param request
     * @return
     */
    Result<Void> taskSyncFallback(@RequestBody FunctionTreeTaskSyncJournalResponse request);
}

