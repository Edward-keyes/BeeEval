package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.ErrorTypeEntity;
import com.xailab.vehicle.operation.testplatform.vo.ErrorTypeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface ErrorTypeConvert {
    ErrorTypeConvert INSTANCE = Mappers.getMapper(ErrorTypeConvert.class);

    ErrorTypeEntity convert(ErrorTypeVO vo);

    ErrorTypeVO convert(ErrorTypeEntity entity);

    List<ErrorTypeVO> convertList(List<ErrorTypeEntity> list);

}