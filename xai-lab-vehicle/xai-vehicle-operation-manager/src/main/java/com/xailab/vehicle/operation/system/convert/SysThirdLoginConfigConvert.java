package com.xailab.vehicle.operation.system.convert;

import com.xailab.vehicle.operation.system.entity.SysThirdLoginConfigEntity;
import com.xailab.vehicle.operation.system.vo.SysThirdLoginConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 第三方登录配置
 *

 */
@Mapper
public interface SysThirdLoginConfigConvert {
    SysThirdLoginConfigConvert INSTANCE = Mappers.getMapper(SysThirdLoginConfigConvert.class);

    SysThirdLoginConfigEntity convert(SysThirdLoginConfigVO vo);

    SysThirdLoginConfigVO convert(SysThirdLoginConfigEntity entity);

    List<SysThirdLoginConfigVO> convertList(List<SysThirdLoginConfigEntity> list);

}