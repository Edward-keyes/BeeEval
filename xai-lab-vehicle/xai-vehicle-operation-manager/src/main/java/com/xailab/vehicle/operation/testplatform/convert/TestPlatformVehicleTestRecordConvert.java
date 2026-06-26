package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestRecordEntity;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleTestRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* test_record
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@Mapper
public interface TestPlatformVehicleTestRecordConvert {
    TestPlatformVehicleTestRecordConvert INSTANCE = Mappers.getMapper(TestPlatformVehicleTestRecordConvert.class);

    TestPlatformVehicleTestRecordEntity convert(TestPlatformVehicleTestRecordVO vo);

    TestPlatformVehicleTestRecordVO convert(TestPlatformVehicleTestRecordEntity entity);

    List<TestPlatformVehicleTestRecordVO> convertList(List<TestPlatformVehicleTestRecordEntity> list);

}