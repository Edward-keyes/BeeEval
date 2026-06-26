package com.xailab.vehicle.operation.system.convert;

import com.xailab.vehicle.operation.sms.config.SmsConfig;
import com.xailab.vehicle.operation.system.entity.SysSmsConfigEntity;
import com.xailab.vehicle.operation.system.vo.SysSmsConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 短信配置
 *
 * 
 */
@Mapper
public interface SysSmsConfigConvert {
    SysSmsConfigConvert INSTANCE = Mappers.getMapper(SysSmsConfigConvert.class);

    SysSmsConfigEntity convert(SysSmsConfigVO vo);

    SysSmsConfigVO convert(SysSmsConfigEntity entity);

    List<SysSmsConfigVO> convertList(List<SysSmsConfigEntity> list);

    SmsConfig convert2(SysSmsConfigEntity entity);

    List<SmsConfig> convertList2(List<SysSmsConfigEntity> list);

}