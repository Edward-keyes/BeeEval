package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.QuesStateOptionsEntity;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateOptionsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 问卷选项 选择结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface QuesStateOptionsConvert {
    QuesStateOptionsConvert INSTANCE = Mappers.getMapper(QuesStateOptionsConvert.class);

    QuesStateOptionsEntity convert(QuesStateOptionsVO vo);

    QuesStateOptionsVO convert(QuesStateOptionsEntity entity);

    List<QuesStateOptionsVO> convertList(List<QuesStateOptionsEntity> list);

}