package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;
import com.xailab.vehicle.operation.testplatform.pojo.response.FunctionTreeCaseStateResponse;
import com.xailab.vehicle.operation.testplatform.pojo.response.TestStateInfoResponse;
import com.xailab.vehicle.operation.testplatform.vo.TestCaseMaterialVo;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleTestStateVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 测试记录用例表
*
* @author mumu 
* @since 1.0.0 2025-04-16
*/
@Mapper
public interface TestPlatformVehicleTestStateConvert {
    TestPlatformVehicleTestStateConvert INSTANCE = Mappers.getMapper(TestPlatformVehicleTestStateConvert.class);

    TestPlatformVehicleTestStateEntity convert(TestPlatformVehicleTestStateVO vo);

    TestPlatformVehicleTestStateVO convert(TestPlatformVehicleTestStateEntity entity);

    List<TestPlatformVehicleTestStateVO> convertList(List<TestPlatformVehicleTestStateEntity> list);

    @Mapping(target ="testCaseType",source = "testCaseType")
    FunctionTreeCaseStateResponse convertRes(TestPlatformVehicleTestStateEntity entity,Integer testCaseType);


    @Mapping(target = "materialVos",source = "materialVos")
    TestStateInfoResponse convertRes(TestPlatformVehicleTestStateEntity entity, List<TestCaseMaterialVo> materialVos);

}