package com.securitytest.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.securitytest.web.entities.SysRole;

public interface SysRoleService extends IService<SysRole> {
    
    /**
     * 通过角色id查询角色信息和权限信息
     * @param id
     * @return
     */
    SysRole findById(Long id);
    
    boolean deleteById(Long id);
}
