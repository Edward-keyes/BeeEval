package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestCaseEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeCaseProcessInfo;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeCaseWalkthroughResponse;
import com.xailab.vehicle.operation.testplatform.vo.TestProcessVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 流程数据表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface TestProcessConvert {
    TestProcessConvert INSTANCE = Mappers.getMapper(TestProcessConvert.class);

    TestProcessEntity convert(TestProcessVO vo);

    TestProcessVO convert(TestProcessEntity entity);

    List<TestProcessVO> convertList(List<TestProcessEntity> list);

    @Mapping(target = "levelOneName",source = "functionDomainName")
    @Mapping(target = "levelTwoName",source = "vo.scenarioTask")
    @Mapping(target = "levelThreeName",source = "vo.taskDetail")
    TestProcessEntity convert(TestPlatformVehicleTestCaseEntity vo,String functionDomainName);


    FunctionTreeCaseProcessInfo convertInfo(TestProcessEntity entity);

    @Mapping(target = "id",ignore = true)
    void convertEntity(FunctionTreeCaseProcessInfo processInfo, @MappingTarget TestProcessEntity entity);


    @Mapping(target ="levelTwoName",source = "levelTwoName")
    @Mapping(target ="levelOneName",source = "response.functionDomainName")
    @Mapping(target ="quesOne",source = "response.alternativeQuestions.quesOne")
    @Mapping(target ="quesTwo",source = "response.alternativeQuestions.quesTwo")
    @Mapping(target ="quesThree",source = "response.alternativeQuestions.quesThree")
    @Mapping(target ="optionsTitle",source = "response.optionSettings.optionsTitle")
    @Mapping(target ="optionsType",source = "response.optionSettings.optionsType")
    TestProcessEntity convertEntity(FunctionTreeCaseWalkthroughResponse response, String levelTwoName );

}