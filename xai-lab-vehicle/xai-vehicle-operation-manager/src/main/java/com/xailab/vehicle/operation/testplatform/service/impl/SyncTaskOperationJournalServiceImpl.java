package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.framework.security.user.SecurityUser;
import com.xailab.vehicle.framework.security.user.UserDetail;
import com.xailab.vehicle.operation.testplatform.dao.SyncTaskOperationJournalDao;
import com.xailab.vehicle.operation.testplatform.entity.SyncTaskOperationJournalEntity;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTaskOperationEnum;
import com.xailab.vehicle.operation.testplatform.service.SyncTaskOperationJournalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName: SyncTaskOperationJournalServiceImpl
 * @Description:
 * @author: liulin
 * @date: 2025/7/8 0:17
 */
@Service
@Slf4j
@DS("test_platform")
public class SyncTaskOperationJournalServiceImpl extends BaseServiceImpl<SyncTaskOperationJournalDao, SyncTaskOperationJournalEntity> implements SyncTaskOperationJournalService {


    /**
     * 保存同步任务操作日志
     * @param taskSerial
     * @param operationType
     * @param operationResult
     * @param operationMessage
     */
    @Override
    public Long saveTaskOperationJournal(String taskSerial, FunctionTreeTaskOperationEnum operationType,
                                         Boolean operationResult, String operationMessage) {
        //获取操作人
        UserDetail user = SecurityUser.getUser();
        SyncTaskOperationJournalEntity journalEntity = new SyncTaskOperationJournalEntity();
        journalEntity.setTaskSerial(taskSerial);
        journalEntity.setOperationType(operationType.getName());
        journalEntity.setOperationResult(operationResult);
        journalEntity.setOperationMessage(operationMessage);
        journalEntity.setOperationUser(user != null ? user.getUsername() : null);
        journalEntity.setOperationTime(new Date());
        this.save(journalEntity);
        log.info("保存同步任务操作日志:{}",journalEntity);
        return journalEntity.getId();
    }
}
