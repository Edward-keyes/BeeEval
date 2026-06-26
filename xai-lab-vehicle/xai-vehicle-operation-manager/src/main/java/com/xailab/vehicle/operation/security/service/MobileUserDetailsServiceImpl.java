package com.xailab.vehicle.operation.security.service;

import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.security.mobile.MobileUserDetailsService;
import com.xailab.vehicle.operation.system.convert.SysUserConvert;
import com.xailab.vehicle.operation.system.dao.SysUserDao;
import com.xailab.vehicle.operation.system.entity.SysUserEntity;
import com.xailab.vehicle.operation.system.service.SysUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 手机验证码登录 MobileUserDetailsService
 *

 */
@Service
@AllArgsConstructor
public class MobileUserDetailsServiceImpl implements MobileUserDetailsService {
    private final SysUserDetailsService sysUserDetailsService;
    private final SysUserDao sysUserDao;

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        SysUserEntity userEntity = sysUserDao.getByMobile(mobile);
        if (userEntity == null) {
            throw new UsernameNotFoundException("手机号或验证码错误");
        }

        return sysUserDetailsService.getUserDetails(SysUserConvert.INSTANCE.convertDetail(userEntity));
    }

}
