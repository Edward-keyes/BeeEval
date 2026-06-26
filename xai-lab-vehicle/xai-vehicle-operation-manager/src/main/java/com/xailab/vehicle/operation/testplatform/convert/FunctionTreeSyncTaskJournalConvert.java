package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.feign.pojo.response.FunctionTreeTaskSyncJournalResponse;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskJournalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FunctionTreeSyncTaskJournalConvert {

    FunctionTreeSyncTaskJournalConvert INSTANCE = Mappers.getMapper(FunctionTreeSyncTaskJournalConvert.class);


    FunctionTreeSyncTaskJournalEntity convert(FunctionTreeTaskSyncJournalResponse response);

}
