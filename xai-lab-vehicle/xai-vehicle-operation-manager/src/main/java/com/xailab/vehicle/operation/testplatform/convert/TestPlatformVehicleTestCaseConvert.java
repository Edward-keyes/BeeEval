package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestCaseEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestScenarioEntity;
import com.xailab.vehicle.operation.testplatform.pojo.excel.TestCaseBathAddImportTemplate;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeAddRequest;
import com.xailab.vehicle.operation.testplatform.pojo.request.FunctionTreeTestCaseAddRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeSyncAllTreeResponse;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeSyncTreeListResponse;
import com.xailab.vehicle.operation.testplatform.pojo.response.TestPlatformFunctionTreeCaseResponse;
import com.xailab.vehicle.operation.testplatform.pojo.response.TestPlatformFunctionTreeResponse;
import com.xailab.vehicle.operation.testplatform.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 测试用例总表
*
* @author mumu 
* @since 1.0.0 2025-04-16
*/
@Mapper
public interface TestPlatformVehicleTestCaseConvert {
    TestPlatformVehicleTestCaseConvert INSTANCE = Mappers.getMapper(TestPlatformVehicleTestCaseConvert.class);

    TestPlatformVehicleTestCaseEntity convert(TestPlatformVehicleTestCaseVO vo);

    TestPlatformVehicleTestCaseVO convert(TestPlatformVehicleTestCaseEntity entity);

    List<TestPlatformVehicleTestCaseVO> convertList(List<TestPlatformVehicleTestCaseEntity> list);

    TestPlatformVehicleTestCaseEntity convert(TestPlatformVehicleTestCaseAddVO vo);

    TestPlatformVehicleTestCaseEntity convert(TestPlatformVehicleTestCaseUpdateVO vo);

    @Mappings({
       @Mapping(target = "industryAverage",ignore = true),
       @Mapping(target = "varianceValue",ignore = true)
    })
    TestPlatformVehicleTestCaseVO convert(TestPlatformTestCasePageVo entity);

    TestPlatformVehicleTestCaseEntity convert(TestCaseBathAddImportTemplate vo);
    List<TestPlatformVehicleTestCaseEntity> convertListExcel(List<TestCaseBathAddImportTemplate> vo);

    @Mappings({
            @Mapping(source = "taskDetail",target = "functionTag")
    })
    TestPlatformFunctionTreeResponse convert(TestPlatformFunctionTreeVo vo);

    @Mappings({
            @Mapping(source = "taskDetail",target = "functionTag")
    })
    FunctionTreeSyncAllTreeResponse convertResV2(TestPlatformFunctionTreeVo vo);

    @Mappings({
            @Mapping(source = "taskDetail",target = "functionTag")
    })
    FunctionTreeSyncTreeListResponse convertRes(TestPlatformFunctionTreeVo vo);

    List<TestPlatformFunctionTreeCaseResponse> convertListRes(List<FunctionTreeCaseVo> entities);

    TestPlatformFunctionTreeCaseResponse convertRes(FunctionTreeCaseVo entity);

    @Mapping(target ="functionDomainName",source ="vo.functionDomainName")
    @Mapping(target ="functionId",source ="vo.functionId")
    @Mapping(target ="scenarioTask",source ="entity.scenarioTask")
    @Mapping(target ="taskDetail",source ="entity.taskDetail")
    TestPlatformFunctionTreeCaseResponse convertRes(FunctionTreeCaseVo entity,TestPlatformFunctionTreeVo vo);

    @Mapping(target ="testcaseContent",source ="request.testcaseContent" )
    @Mapping(target ="functionId",source ="request.functionId")
    @Mapping(target ="scenarioTask",source ="request.scenarioTask")
    @Mapping(target ="taskDetail",source ="request.taskDetail")
    @Mapping(target ="scenarioId",source ="scenarioEntity.id")
    @Mapping(target ="tertiaryMetric",source ="scenarioEntity.scenarioName" )
    @Mapping(target = "id",ignore = true)
    TestPlatformVehicleTestCaseEntity convert(FunctionTreeAddRequest request, TestPlatformVehicleTestScenarioEntity scenarioEntity);


    @Mapping(target ="testcaseContent",source ="request.testcaseContent" )
    @Mapping(target ="functionId",source ="request.functionId")
    @Mapping(target ="scenarioTask",source ="request.scenarioTask")
    @Mapping(target ="taskDetail",source ="request.functionTag")

    @Mapping(target ="scenarioId",source ="scenarioEntity.id")
    @Mapping(target ="tertiaryMetric",source ="scenarioEntity.scenarioName" )
    @Mapping(target = "id",ignore = true)
    TestPlatformVehicleTestCaseEntity convert(FunctionTreeTestCaseAddRequest request, TestPlatformVehicleTestScenarioEntity scenarioEntity);
}