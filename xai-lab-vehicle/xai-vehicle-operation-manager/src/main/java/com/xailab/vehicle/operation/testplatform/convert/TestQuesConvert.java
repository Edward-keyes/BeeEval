package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestQuesEntity;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeScoreQuestionAddRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeScoreQuestionResponse;
import com.xailab.vehicle.operation.testplatform.vo.TestQuesVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 功能评价数据表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface TestQuesConvert {
    TestQuesConvert INSTANCE = Mappers.getMapper(TestQuesConvert.class);

    TestQuesEntity convert(TestQuesVO vo);

    TestQuesVO convert(TestQuesEntity entity);

    List<TestQuesVO> convertList(List<TestQuesEntity> list);


    FunctionTreeScoreQuestionResponse convertRes(TestQuesEntity entity);
    TestQuesEntity convert(FunctionTreeScoreQuestionAddRequest entity);

    TestQuesEntity convert(FunctionTreeScoreQuestionResponse response);

}