package com.securitytest.security;

import com.securitytest.web.entities.SysPermission;
import com.securitytest.web.entities.SysUser;
import com.securitytest.web.service.SysPermissionService;
import com.securitytest.web.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUserDetailsService implements UserDetailsService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SysUserService sysUserService;
	
	@Autowired
	private SysPermissionService sysPermissionService;
	
	public abstract SysUser findSysUser(String userNameOrMobile);
	
	@Override
	public UserDetails loadUserByUsername(String userNameOrMobile) throws UsernameNotFoundException {
		//1.通过请求的用户名去数据库中查询用户信息
		SysUser sysUser = findSysUser(userNameOrMobile);
		//通过用户id获取权限信息
		findSysPermission(sysUser);
		return sysUser;
	}
	
	private void findSysPermission(SysUser sysUser) {
		//2.查询该用户有哪一些权限
		List<SysPermission> sysPermissions = sysPermissionService.findByUserId(sysUser.getId());
		if (CollectionUtils.isEmpty(sysPermissions)) {
			return;
		}
		//3.封装用户信息和权限信息
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (SysPermission sysPermission : sysPermissions) {
			authorities.add(new SimpleGrantedAuthority(sysPermission.getCode()));
		}
		sysUser.setAuthorities(authorities);
		//在左侧菜单动态渲染时会用到,当前认证时用不到
		sysUser.setPermissions(sysPermissions);
	}
}
