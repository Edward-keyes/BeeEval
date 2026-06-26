package com.xailab.vehicle.operation.system.convert;

import com.xailab.vehicle.operation.system.entity.SysMailLogEntity;
import com.xailab.vehicle.operation.system.vo.SysMailLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 邮件日志
 *
 * 
 */
@Mapper
public interface SysMailLogConvert {
    SysMailLogConvert INSTANCE = Mappers.getMapper(SysMailLogConvert.class);

    SysMailLogEntity convert(SysMailLogVO vo);

    SysMailLogVO convert(SysMailLogEntity entity);

    List<SysMailLogVO> convertList(List<SysMailLogEntity> list);

}