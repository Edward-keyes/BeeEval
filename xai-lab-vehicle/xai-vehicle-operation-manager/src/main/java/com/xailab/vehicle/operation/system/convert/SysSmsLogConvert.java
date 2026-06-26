package com.xailab.vehicle.operation.system.convert;

import com.xailab.vehicle.operation.system.entity.SysSmsLogEntity;
import com.xailab.vehicle.operation.system.vo.SysSmsLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 短信日志
 *
 *
 */
@Mapper
public interface SysSmsLogConvert {
    SysSmsLogConvert INSTANCE = Mappers.getMapper(SysSmsLogConvert.class);

    SysSmsLogVO convert(SysSmsLogEntity entity);

    List<SysSmsLogVO> convertList(List<SysSmsLogEntity> list);

}