package com.securitytest.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.securitytest.web.entities.SysUser;

public interface SysUserService extends IService<SysUser> {
	
	/**
	 * 通过用户名查找用户信息
	 * @param username
	 * @return
	 */
	SysUser findByUsername(String username);
	
	/**
	 * 通过手机号查找用户信息
	 * @param mobile
	 * @return
	 */
	SysUser findByMobile(String mobile);
	
	/**
	 * 分页查询用户信息
	 * @param page  分页对象
	 * @param sysUser   查询条件
	 * @return
	 */
	IPage<SysUser> selectPage(Page<SysUser> page, SysUser sysUser);
	
	/**
	 * 1.用户id查询用户信息sys_user
	 * 2.用户id查询所拥有的角色
	 * @param id
	 * @return
	 */
	SysUser findById(Long id);
	
	/**
	 * 假删除，将is_enable改为0
	 * @param id
	 * @return
	 */
	boolean deleteById(Long id);
}
