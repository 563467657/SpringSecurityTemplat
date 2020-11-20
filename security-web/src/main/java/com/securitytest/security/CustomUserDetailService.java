package com.securitytest.security;

import com.securitytest.web.entities.SysUser;
import com.securitytest.web.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 查询数据库中的用户信息
 */
@Component("customUserDetailsService")
public class CustomUserDetailService extends AbstractUserDetailsService {
//public class CustomUserDetailService implements UserDetailsService {

	@Autowired  //不能删掉，不然会报错
	private PasswordEncoder passwordEncoder;
	
	@Override
	public SysUser findSysUser(String userNameOrMobile) {
		logger.info("请求认证的用户名：" + userNameOrMobile);
		//1.通过请求的用户名去数据库中查询用户信息
		SysUser sysUser = sysUserService.findByUsername(userNameOrMobile);
		if (sysUser == null) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}
		return sysUser;
	}
	
}
