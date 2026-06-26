package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskInfoEntity;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncTaskInfoRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeSyncTaskInfoResponse;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncTaskInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 功能树同步任务详情表
*
* @author mm 
* @since 1.0.0 2025-06-02
*/
@Mapper
public interface FunctionTreeSyncTaskInfoConvert {
    FunctionTreeSyncTaskInfoConvert INSTANCE = Mappers.getMapper(FunctionTreeSyncTaskInfoConvert.class);

    FunctionTreeSyncTaskInfoEntity convert(FunctionTreeSyncTaskInfoVO vo);

    FunctionTreeSyncTaskInfoVO convert(FunctionTreeSyncTaskInfoEntity entity);

    List<FunctionTreeSyncTaskInfoVO> convertList(List<FunctionTreeSyncTaskInfoEntity> list);
    @Mapping(target = "testCaseId",ignore = true)
    FunctionTreeSyncTaskInfoEntity convert(FunctionTreeSyncTaskInfoRequest entity);

    List<FunctionTreeSyncTaskInfoEntity> convertListReq(List<FunctionTreeSyncTaskInfoRequest> list);

    @Mapping(source = "traceId", target= "taskSerial")
    @Mapping(target = "testCaseId",ignore = true)
    FunctionTreeSyncTaskInfoEntity convertEntity(FunctionTreeSyncTaskInfoRequest entity,String traceId);

    FunctionTreeSyncTaskInfoResponse convertRes(FunctionTreeSyncTaskInfoEntity entity);
}