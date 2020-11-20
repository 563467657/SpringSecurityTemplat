package com.securitytest.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.securitytest.web.entities.SysPermission;
import com.securitytest.web.mapper.SysPermissionMapper;
import com.securitytest.web.service.SysPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
	@Override
	public List<SysPermission> findByUserId(Long userId) {
		if (userId == null) {
			return null;
		}
		List<SysPermission> sysPermissions = baseMapper.selectPermissionByUserId(userId);
		return sysPermissions;
	}
	
	@Transactional
	@Override
	public boolean deleteById(Long id) {
		//1、删除当前id的权限
		baseMapper.deleteById(id);
		//2、删除parent_id = id 的权限，删除当前节点的子权限
		LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper();
		queryWrapper.eq(SysPermission::getParentId, id);
		baseMapper.delete(queryWrapper);
		return true;
	}
}
