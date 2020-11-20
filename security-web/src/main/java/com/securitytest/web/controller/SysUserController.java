package com.securitytest.web.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.securitytest.base.result.JsonResult;
import com.securitytest.web.entities.SysRole;
import com.securitytest.web.entities.SysUser;
import com.securitytest.web.service.SysRoleService;
import com.securitytest.web.service.SysUserService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/user")
public class SysUserController {
	
	private static final String HTML_PREFIX = "system/user/";
	
	@PreAuthorize("hasAuthority('sys:user')")
	@GetMapping(value = {"/", ""})
	public String user() {
		return HTML_PREFIX + "user-list";
	}
	
//	/**
//	 * 跳转到新增或者修改页面
//	 * 有'sys:user:add'或'sys:user:edit'权限的用户可以访问
//	 * @return
//	 */
//	@PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
//	@RequestMapping(value = {"/form"})
//	public String form() {
//		return HTML_PREFIX + "user-form";
//	}
//
//	/**
//	 * 返回值的code等于200则调用成功有权限，否则为403
//	 * @param id
//	 * @return
//	 */
//	@PostAuthorize("returnObject.code == 200")
//	@RequestMapping("/{id}")
//	@ResponseBody
//	public JsonResult deleteById(@PathVariable Long id) {
//		if (id < 0) {
//			return JsonResult.build(500, "失败");
//		}
//		return JsonResult.ok();
//	}
//
//	//过滤请求参数:filterTarget 指定哪个参数,filterObject是集合中的每个元素，如果value表达式为true的数据则不会被过滤，否则就过滤掉
//	@PreFilter(filterTarget = "ids", value = "filterObject > 0")
//	@RequestMapping("/batch/{ids}")
//	@ResponseBody
//	public JsonResult deleteByIds(@PathVariable List<Long> ids) {
//		return JsonResult.ok(ids);
//	}
//
//	@PostFilter("filterObject != authentication.principal.username")
//	@RequestMapping("/list")
//	@ResponseBody
//	public List<String> page() {
//		List<String> userList = Lists.newArrayList("winter", "xue", "gu");
//		return userList;
//	}
	
	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 分页查询用户列表
	 * @param page  分页对象：size,current
	 * @param sysUser   查询条件：username,mobile
	 * @return
	 */
	@PreAuthorize("hasAuthority('sys:user:list')")
	@PostMapping("/page")
	@ResponseBody
	public JsonResult page(Page<SysUser> page, SysUser sysUser) {
		return JsonResult.ok(sysUserService.selectPage(page, sysUser));
	}
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
	@GetMapping(value = {"/form","/form/{id}"})
	public String form(@PathVariable(required = false) Long id, Model model) {
		//1.查询用户信息，包含了用户所拥有的角色
		SysUser user = sysUserService.findById(id);
		model.addAttribute("user", user);
		//2.查询出所有的角色信息
		List<SysRole> roleList = sysRoleService.list();
		model.addAttribute("roleList", roleList);
		return HTML_PREFIX + "user-form";
	}
	
	@PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT},value = "")
	public String saveOrUpdate(SysUser sysUser) {
		sysUserService.saveOrUpdate(sysUser);
		return "redirect:/user";
	}
	
	@PreAuthorize("hasAuthority('sys:user:delete')")
	@DeleteMapping(value = "/{id}")
	@ResponseBody
	public JsonResult delete(@PathVariable Long id) {
		sysUserService.deleteById(id);
		return JsonResult.ok();
	}
}
