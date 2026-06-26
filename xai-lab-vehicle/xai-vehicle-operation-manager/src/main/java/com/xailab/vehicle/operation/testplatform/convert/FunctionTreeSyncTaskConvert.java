package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.feign.vo.FunctionTreeSyncTaskVo;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncAuditJournalEntity;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskEntity;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeSyncCreateRequest;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncAuditJournalVO;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncTaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 功能树数据同步表
*
* @author mumu 
* @since 1.0.0 2025-06-02
*/
@Mapper
public interface FunctionTreeSyncTaskConvert {
    FunctionTreeSyncTaskConvert INSTANCE = Mappers.getMapper(FunctionTreeSyncTaskConvert.class);

    FunctionTreeSyncTaskEntity convert(FunctionTreeSyncTaskVO vo);

    FunctionTreeSyncTaskEntity convert(FunctionTreeSyncTaskVo vo);

    FunctionTreeSyncTaskVO convert(FunctionTreeSyncTaskEntity entity);

    List<FunctionTreeSyncTaskVO> convertList(List<FunctionTreeSyncTaskEntity> list);

    @Mapping(target = "taskSerial",source = "taskSerial")
    @Mapping(target = "creator",source = "userId")
    FunctionTreeSyncTaskEntity convert(FunctionTreeSyncCreateRequest vo,String taskSerial,Long userId);


    List<FunctionTreeSyncAuditJournalVO> convertAuditJournalList(List<FunctionTreeSyncAuditJournalEntity> list);

    FunctionTreeSyncAuditJournalVO convertAuditJournal(FunctionTreeSyncAuditJournalEntity entity);
}