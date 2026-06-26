package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.SyncTaskOperationJournalEntity;
import com.xailab.vehicle.operation.testplatform.vo.SyncTaskOperationJournalVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ClassName: SyncTaskOperationJournalConvert
 * @Description:
 * @author: liulin
 * @date: 2025/7/9 23:41
 */
@Mapper
public interface SyncTaskOperationJournalConvert {
    SyncTaskOperationJournalConvert INSTANCE = Mappers.getMapper(SyncTaskOperationJournalConvert.class);

    SyncTaskOperationJournalVO convert(SyncTaskOperationJournalEntity entity);

    List<SyncTaskOperationJournalVO> convertList(List<SyncTaskOperationJournalEntity> list);


}
