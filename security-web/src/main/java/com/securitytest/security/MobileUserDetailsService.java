package com.securitytest.security;

import com.securitytest.web.entities.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 通过手机号获取用户信息和权限资源
 */
@Component("mobileUserDetailsService")
public class MobileUserDetailsService extends AbstractUserDetailsService {
//public class MobileUserDetailsService implements UserDetailsService {
	
	@Override
	public SysUser findSysUser(String userNameOrMobile) {
		logger.info("请求的手机号是：" + userNameOrMobile);
		//1.通过手机号查询用户信息
		SysUser sysUser = sysUserService.findByMobile(userNameOrMobile);
		if (sysUser == null) {
			throw new UsernameNotFoundException("该手机号未注册");
		}
		return sysUser;
	}
	
	
}
