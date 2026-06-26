package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestScenarioEntity;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformTestScenarioResVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 方案细分表
*
* @author mumu
* @since 1.0.0 2025-04-16
*/
@Mapper
public interface TestPlatformVehicleTestScenarioConvert {
    TestPlatformVehicleTestScenarioConvert INSTANCE = Mappers.getMapper(TestPlatformVehicleTestScenarioConvert.class);

    TestPlatformTestScenarioResVo convert(TestPlatformVehicleTestScenarioEntity vo);
    List<TestPlatformTestScenarioResVo> convert(List<TestPlatformVehicleTestScenarioEntity> vo);


}