package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.QuesStateEntity;
import com.xailab.vehicle.operation.testplatform.vo.QuesStateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 功能评价结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface QuesStateConvert {
    QuesStateConvert INSTANCE = Mappers.getMapper(QuesStateConvert.class);

    QuesStateEntity convert(QuesStateVO vo);

    QuesStateVO convert(QuesStateEntity entity);

    List<QuesStateVO> convertList(List<QuesStateEntity> list);

}