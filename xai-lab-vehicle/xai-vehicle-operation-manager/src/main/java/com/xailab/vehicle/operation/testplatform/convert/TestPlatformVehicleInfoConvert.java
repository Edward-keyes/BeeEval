package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleInfoEntity;
import com.xailab.vehicle.operation.testplatform.vo.TestPlatformVehicleInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 车辆信息表
*
* @author mm 
* @since 1.0.0 2025-04-17
*/
@Mapper
public interface TestPlatformVehicleInfoConvert {
    TestPlatformVehicleInfoConvert INSTANCE = Mappers.getMapper(TestPlatformVehicleInfoConvert.class);

    TestPlatformVehicleInfoEntity convert(TestPlatformVehicleInfoVO vo);

    @Mappings({
            @Mapping(source = "id",target = "vehicleId"),
            @Mapping(source = "id",target = "id")
    })
    TestPlatformVehicleInfoVO convert(TestPlatformVehicleInfoEntity entity);

    List<TestPlatformVehicleInfoVO> convertList(List<TestPlatformVehicleInfoEntity> list);

}