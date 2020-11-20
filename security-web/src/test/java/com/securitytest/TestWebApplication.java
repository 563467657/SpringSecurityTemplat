package com.securitytest;

import com.securitytest.web.entities.SysRole;
import com.securitytest.web.entities.SysUser;
import com.securitytest.web.service.SysPermissionService;
import com.securitytest.web.service.SysRoleService;
import com.securitytest.web.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWebApplication {
	
	@Autowired
	SysUserService sysUserService;
	
	@Test
	public void testSysUser(){
		List<SysUser> list = sysUserService.list();
		System.out.println(list);
		SysUser admin = sysUserService.findByUsername("admin");
		System.out.println(admin);
	}
	
	@Autowired
	SysRoleService sysRoleService;
	
	@Test
	public void testSysRole() {
		SysRole byId = sysRoleService.getById(9);
		System.out.println(byId);
	}
	
	@Autowired
	private SysPermissionService sysPermissionService;
	
	@Test
	public void testSysPermission() {
		System.out.println(sysPermissionService.findByUserId(11l).size());
	}
}
