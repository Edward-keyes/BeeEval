package com.xailab.vehicle.operation.security.service;

import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.security.third.ThirdUserDetailsService;
import com.xailab.vehicle.operation.system.convert.SysUserConvert;
import com.xailab.vehicle.operation.system.dao.SysUserDao;
import com.xailab.vehicle.operation.system.entity.SysUserEntity;
import com.xailab.vehicle.operation.system.service.SysThirdLoginService;
import com.xailab.vehicle.operation.system.service.SysUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 第三方登录，ThirdUserDetailsService
 *

 */
@Service
@AllArgsConstructor
public class ThirdUserDetailsServiceImpl implements ThirdUserDetailsService {
    private final SysUserDetailsService sysUserDetailsService;
    private final SysThirdLoginService sysThirdLoginService;
    private final SysUserDao sysUserDao;

    @Override
    public UserDetails loadUserByOpenTypeAndOpenId(String openType, String openId) throws UsernameNotFoundException {
        Long userId = sysThirdLoginService.getUserIdByOpenTypeAndOpenId(openType, openId);
        SysUserEntity userEntity = sysUserDao.getById(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("绑定的系统用户，不存在");
        }

        return sysUserDetailsService.getUserDetails(SysUserConvert.INSTANCE.convertDetail(userEntity));
    }
}
