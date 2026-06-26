package com.xailab.vehicle.operation.testplatform.service;

import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.testplatform.entity.SyncTaskOperationJournalEntity;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTaskOperationEnum;

public interface SyncTaskOperationJournalService extends BaseService<SyncTaskOperationJournalEntity> {

    Long saveTaskOperationJournal(String taskSerial, FunctionTreeTaskOperationEnum operationType, Boolean operationResult, String operationMessage);

}
