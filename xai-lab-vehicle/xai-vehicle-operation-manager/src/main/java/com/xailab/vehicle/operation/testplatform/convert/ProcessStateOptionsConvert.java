package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.ProcessStateOptionsEntity;
import com.xailab.vehicle.operation.testplatform.vo.ProcessStateOptionsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 流程多选项 选择结果
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface ProcessStateOptionsConvert {
    ProcessStateOptionsConvert INSTANCE = Mappers.getMapper(ProcessStateOptionsConvert.class);

    ProcessStateOptionsEntity convert(ProcessStateOptionsVO vo);

    ProcessStateOptionsVO convert(ProcessStateOptionsEntity entity);

    List<ProcessStateOptionsVO> convertList(List<ProcessStateOptionsEntity> list);

}