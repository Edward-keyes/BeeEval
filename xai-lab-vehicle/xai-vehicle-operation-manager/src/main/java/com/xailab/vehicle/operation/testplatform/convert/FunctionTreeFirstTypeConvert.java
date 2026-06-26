package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeFirstTypeEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeLevelTypeResponse;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeFirstTypeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 功能树一级标签类型
*
* @author mu 
* @since  2025-05-31
*/
@Mapper
public interface FunctionTreeFirstTypeConvert {
    FunctionTreeFirstTypeConvert INSTANCE = Mappers.getMapper(FunctionTreeFirstTypeConvert.class);

    FunctionTreeFirstTypeEntity convert(FunctionTreeFirstTypeVO vo);

    FunctionTreeFirstTypeVO convert(FunctionTreeFirstTypeEntity entity);

    List<FunctionTreeFirstTypeVO> convertList(List<FunctionTreeFirstTypeEntity> list);

    FunctionTreeLevelTypeResponse convertRes(FunctionTreeFirstTypeEntity entity);

    List<FunctionTreeLevelTypeResponse> convertListRes(List<FunctionTreeFirstTypeEntity> list);

}