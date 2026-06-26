package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeStateEntity;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeStateEditRequest;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeStateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 功能树状态表
*
* @author mumu 
* @since 1.0.0 2025-05-18
*/
@Mapper
public interface FunctionTreeStateConvert {
    FunctionTreeStateConvert INSTANCE = Mappers.getMapper(FunctionTreeStateConvert.class);

    FunctionTreeStateEntity convert(FunctionTreeStateVO vo);

    FunctionTreeStateVO convert(FunctionTreeStateEntity entity);

    List<FunctionTreeStateVO> convertList(List<FunctionTreeStateEntity> list);

    FunctionTreeStateEntity convert(FunctionTreeStateEditRequest entity);



}