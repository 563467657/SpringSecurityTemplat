package com.securitytest.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.securitytest.web.entities.SysPermission;
import com.securitytest.web.entities.SysRole;
import com.securitytest.web.entities.SysRolePermission;
import com.securitytest.web.mapper.SysPermissionMapper;
import com.securitytest.web.mapper.SysRoleMapper;
import com.securitytest.web.mapper.SysRolePermissionMapper;
import com.securitytest.web.service.SysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    
    @Override
    public SysRole findById(Long id) {
        //1.通过角色id查询对应的角色信息
        SysRole sysRole = baseMapper.selectById(id);
        //2. 通过角色id查询所拥有的权限
        List<SysPermission> permissions = sysPermissionMapper.findByRoleId(id);
        //3.将查询到的权限set到角色对象中
        sysRole.setPerList(permissions);
        return sysRole;
    }
    
    @Transactional
    @Override
    public boolean deleteById(Long id) {
        baseMapper.deleteById(id);
        sysRolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, id));
        return true;
    }
    
    @Transactional
    @Override
    public boolean saveOrUpdate(SysRole entity) {
        //1.更新角色表中的 数据
        entity.setUpdateDate(new Date());
        boolean flag = super.saveOrUpdate(entity);
        if (flag) {
            sysRolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, entity.getId()));
            for (Long perId : entity.getPerIds()) {
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setRoleId(entity.getId());
                sysRolePermission.setPermissionId(perId);
                sysRolePermissionMapper.insert(sysRolePermission);
            }
        }
        return flag;
    }
}
