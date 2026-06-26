package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.ProcessStateEntity;
import com.xailab.vehicle.operation.testplatform.vo.ProcessStateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 流程状态结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface ProcessStateConvert {
    ProcessStateConvert INSTANCE = Mappers.getMapper(ProcessStateConvert.class);

    ProcessStateEntity convert(ProcessStateVO vo);

    ProcessStateVO convert(ProcessStateEntity entity);

    List<ProcessStateVO> convertList(List<ProcessStateEntity> list);

}