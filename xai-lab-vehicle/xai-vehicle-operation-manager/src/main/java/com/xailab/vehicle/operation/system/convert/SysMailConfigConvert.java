package com.xailab.vehicle.operation.system.convert;

import com.xailab.vehicle.operation.email.config.EmailConfig;
import com.xailab.vehicle.operation.system.entity.SysMailConfigEntity;
import com.xailab.vehicle.operation.system.vo.SysMailConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 邮件配置
 *
 *
 */
@Mapper
public interface SysMailConfigConvert {
    SysMailConfigConvert INSTANCE = Mappers.getMapper(SysMailConfigConvert.class);

    SysMailConfigEntity convert(SysMailConfigVO vo);

    SysMailConfigVO convert(SysMailConfigEntity entity);

    List<SysMailConfigVO> convertList(List<SysMailConfigEntity> list);

    EmailConfig convert2(SysMailConfigEntity entity);

    List<EmailConfig> convertList2(List<SysMailConfigEntity> list);

}