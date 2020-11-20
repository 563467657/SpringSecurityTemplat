package com.securitytest.web.controller;

import com.securitytest.base.result.JsonResult;
import com.securitytest.web.entities.SysPermission;
import com.securitytest.web.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理
 */
@Controller
@RequestMapping("/permission")
public class SysPermissionController {
	
	private static final String HTML_PREFIX = "system/permission/";
	
	@PreAuthorize("hasAuthority('sys:permission')")
	@GetMapping(value = {"/", ""})
	public String permission() {
		return HTML_PREFIX + "permission-list";
	}
	
	@Autowired
	private SysPermissionService sysPermissionService;
	
	@PreAuthorize("hasAuthority('sys:permission:list')")
	@PostMapping(value = {"/list"})
	@ResponseBody
	public JsonResult list() {
		List<SysPermission> list = sysPermissionService.list();
		return JsonResult.ok(list);
	}
	
	/**
	 * 跳转新增或者修改页面
	 * @PathVariable(required = false)设置为false，则id可传入也可不传，不然报500错误
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('sys:permission:edit','sys:permission:add')")
	@GetMapping(value = {"/form", "/form/{id}"})
	public String form(@PathVariable(required = false) Long id, Model model) {
		//通过权限id查询对应权限信息
		SysPermission permission = sysPermissionService.getById(id);
		//可以通过前端方式赋值父节点名称
		//permission.setParentName(sysPermissionService.getById(permission.getParentId()).getName());
		//绑定后页面可获取
		model.addAttribute("permission", permission == null ? new SysPermission() : permission);
		return HTML_PREFIX + "permission-form";
	}
	
	@PreAuthorize("hasAnyAuthority('sys:permission:edit','sys:permission:add')")
	@RequestMapping(value = "",method = {RequestMethod.PUT,RequestMethod.POST})
	public String saveOrUpdate(SysPermission permission) {
		sysPermissionService.saveOrUpdate(permission);
		return "redirect:/permission";
	}
	
	@PreAuthorize("hasAuthority('sys:permission:delete')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public JsonResult deleteById(@PathVariable Long id) {
		//TODO 删除方法写死了只能删除两层
		sysPermissionService.deleteById(id);
		return JsonResult.ok();
	}
}
