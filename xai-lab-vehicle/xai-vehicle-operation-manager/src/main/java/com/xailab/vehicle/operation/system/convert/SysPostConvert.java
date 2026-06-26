package com.xailab.vehicle.operation.system.convert;

import com.xailab.vehicle.operation.system.entity.SysPostEntity;
import com.xailab.vehicle.operation.system.vo.SysPostVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface SysPostConvert {
    SysPostConvert INSTANCE = Mappers.getMapper(SysPostConvert.class);

    SysPostVO convert(SysPostEntity entity);

    SysPostEntity convert(SysPostVO vo);

    List<SysPostVO> convertList(List<SysPostEntity> list);

}
