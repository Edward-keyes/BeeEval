package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestProcessOptionsEntity;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessOptionsVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 流程选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface TestProcessOptionsConvert {
    TestProcessOptionsConvert INSTANCE = Mappers.getMapper(TestProcessOptionsConvert.class);

    TestProcessOptionsEntity convert(TestProcessOptionsVO vo);

    TestProcessOptionsVO convert(TestProcessOptionsEntity entity);

    List<TestProcessOptionsVO> convertList(List<TestProcessOptionsEntity> list);

    List<TestProcessOptionsEntity> convertListEntity(List<TestProcessOptionsVO> list);
}