package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehiclePlanDetailEntity;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehiclePlanDetailVO;
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
public interface TestPlatformVehiclePlanDetailConvert {
    TestPlatformVehiclePlanDetailConvert INSTANCE = Mappers.getMapper(TestPlatformVehiclePlanDetailConvert.class);

    TestPlatformVehiclePlanDetailEntity convert(TestPlatformVehiclePlanDetailVO vo);

    TestPlatformVehiclePlanDetailVO convert(TestPlatformVehiclePlanDetailEntity entity);

    List<TestPlatformVehiclePlanDetailVO> convertList(List<TestPlatformVehiclePlanDetailEntity> list);

}