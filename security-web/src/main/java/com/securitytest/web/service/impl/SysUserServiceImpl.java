package com.securitytest.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.securitytest.web.entities.SysRole;
import com.securitytest.web.entities.SysUser;
import com.securitytest.web.entities.SysUserRole;
import com.securitytest.web.mapper.SysRoleMapper;
import com.securitytest.web.mapper.SysUserMapper;
import com.securitytest.web.mapper.SysUserRoleMapper;
import com.securitytest.web.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    //默认密码1234
    private static final String PASSWORD_DEFAULT = "$2a$10$rDkPvvAFV8kqwvKJzwlRv.i.q.wz1w1pz0SFsHn/55jNeZFQv/eCm";
    
    @Autowired
    private SysRoleMapper sysRoleMapper;
    
    @Override
    public SysUser findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }
    
    @Override
    public SysUser findByMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        return baseMapper.selectOne(queryWrapper);
    }
    
    @Override
    public IPage<SysUser> selectPage(Page<SysUser> page, SysUser sysUser) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_enabled", 1);
        if (StringUtils.isNotEmpty(sysUser.getUsername())) {
            queryWrapper.like("username", sysUser.getUsername());
        }
        if (StringUtils.isNotEmpty(sysUser.getMobile())) {
            queryWrapper.eq("mobile", sysUser.getMobile());
        }
        return baseMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public SysUser findById(Long id) {
        if (id == null) {
            return new SysUser();
        }
        //1.用户id查询用户信息sys_user
        SysUser sysUser = baseMapper.selectById(id);
        //2.用户id查询所拥有的角色
        List<SysRole> roleList = sysRoleMapper.findByUserId(id);
        sysUser.setRoleList(roleList);
        return sysUser;
    }
    
    @Override
    public boolean deleteById(Long id) {
        //1.查询用户信息
        SysUser sysUser = baseMapper.selectById(id);
        //2.再更新用户信息，将状态更新为已删除
        sysUser.setEnabled(false);
        sysUser.setUpdateDate(new Date());
        baseMapper.updateById(sysUser);
        return true;
    }
    
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    
    @Transactional
    @Override
    public boolean saveOrUpdate(SysUser entity) {
        if (entity.getId() == null) {
            entity.setPassword(PASSWORD_DEFAULT);
        }
        entity.setUpdateDate(new Date());
        boolean flag = super.saveOrUpdate(entity);
        if (flag) {
            sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, entity.getId()));
            for (Long roleId : entity.getRoleIds()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(entity.getId());
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
        return true;
    }
    
    
}
