package com.securitytest.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.securitytest.base.result.JsonResult;
import com.securitytest.web.entities.SysRole;
import com.securitytest.web.service.SysRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理
 */
@Controller
@RequestMapping("/role")
public class SysRoleController {
	
	private static final String HTML_PREFIX = "system/role/";
	
	@PreAuthorize("hasAuthority('sys:role')")
	@GetMapping(value = {"/", ""})
	public String role() {
		return HTML_PREFIX + "role-list";
	}
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@PreAuthorize("hasAuthority('sys:role:list')")
	@PostMapping("/page")
	@ResponseBody
	public JsonResult page(Page<SysRole> page,SysRole sysRole) {
		IPage<SysRole> iPage = null;
		if (StringUtils.isNotEmpty(sysRole.getName())) {
			iPage = sysRoleService.page(page, new LambdaQueryWrapper<SysRole>().like(SysRole::getName, sysRole.getName()));
		}else{
			iPage = sysRoleService.page(page);
		}
		return JsonResult.ok(iPage);
	}
	
	/**
	 * 跳转新增或修改页面
	 * @param id
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('sys:role:add','sys:role:edit')")
	@GetMapping(value = {"/form", "/form/{id}"})
	public String form(@PathVariable(required = false) Long id, Model model) {
		if(id != null){
			SysRole sysRole = sysRoleService.findById(id);
			model.addAttribute("role", sysRole);
		}else {
			model.addAttribute("role", new SysRole());
		}
		return HTML_PREFIX + "role-form";
	}
	
	/**
	 * 提交新增或者修改的数据
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('sys:role:add','sys:role:edit')")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT},value = "")
	public String saveOrUpdate(SysRole sysRole) {
		sysRoleService.saveOrUpdate(sysRole);
		return "redirect:/role";
	}
	
	@PreAuthorize("hasAuthority('sys:role:delete')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public JsonResult deleteById(@PathVariable Long id) {
		sysRoleService.deleteById(id);
		return JsonResult.ok();
	}
	
}
