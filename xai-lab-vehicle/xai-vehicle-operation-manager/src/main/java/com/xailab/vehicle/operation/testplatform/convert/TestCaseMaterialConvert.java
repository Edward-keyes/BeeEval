package com.xailab.vehicle.operation.testplatform.convert;

import com.xailab.vehicle.feign.pojo.request.FunctionTreeDataSyncCaseInfoDto;
import com.xailab.vehicle.operation.testplatform.entity.TestCaseMaterialEntity;
import com.xailab.vehicle.operation.testplatform.vo.TestCaseMaterialVo;
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
public interface TestCaseMaterialConvert {
    TestCaseMaterialConvert INSTANCE = Mappers.getMapper(TestCaseMaterialConvert.class);

    TestCaseMaterialEntity convert(TestCaseMaterialVo vo);

    TestCaseMaterialVo convert(TestCaseMaterialEntity entity);

    List<TestCaseMaterialVo> convertList(List<TestCaseMaterialEntity> list);

    List<FunctionTreeDataSyncCaseInfoDto.CaseFileMaterial> converResList(List<TestCaseMaterialEntity> list);

    @Mapping(source = "objectName", target = "materialUrl")
    @Mapping(source = "fileType", target = "materialType")
    FunctionTreeDataSyncCaseInfoDto.CaseFileMaterial convertRes(TestCaseMaterialEntity entity);


}