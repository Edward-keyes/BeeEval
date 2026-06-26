package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestQuesOptionsEntity;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesOptionsVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
* 功能评价选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface TestQuesOptionsConvert {
    TestQuesOptionsConvert INSTANCE = Mappers.getMapper(TestQuesOptionsConvert.class);

    TestQuesOptionsEntity convert(TestQuesOptionsVO vo);

    TestQuesOptionsVO convert(TestQuesOptionsEntity entity);

    List<TestQuesOptionsVO> convertList(List<TestQuesOptionsEntity> list);

    @Mapping(target = "quesId", source = "questionId")
    TestQuesOptionsEntity convertEntity(TestQuesOptionsVO entity,Integer questionId);

    List<TestQuesOptionsEntity> convertListEntity(List<TestQuesOptionsVO> list);
}