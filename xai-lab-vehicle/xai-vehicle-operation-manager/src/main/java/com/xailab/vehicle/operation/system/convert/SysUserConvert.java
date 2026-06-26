package com.xailab.vehicle.operation.system.convert;

import com.xailab.vehicle.framework.security.user.UserDetail;
import com.xailab.vehicle.operation.system.entity.SysUserEntity;
import com.xailab.vehicle.operation.system.vo.SysUserBaseVO;
import com.xailab.vehicle.operation.system.vo.SysUserExcelVO;
import com.xailab.vehicle.operation.system.vo.SysUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface SysUserConvert {
    SysUserConvert INSTANCE = Mappers.getMapper(SysUserConvert.class);

    SysUserVO convert(SysUserEntity entity);

    SysUserEntity convert(SysUserVO vo);

    SysUserEntity convert(SysUserBaseVO vo);

    SysUserVO convert(UserDetail userDetail);

    UserDetail convertDetail(SysUserEntity entity);

    List<SysUserVO> convertList(List<SysUserEntity> list);

    List<SysUserExcelVO> convert2List(List<SysUserEntity> list);

    List<SysUserEntity> convertListEntity(List<SysUserExcelVO> list);

}
