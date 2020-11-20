package com.securitytest.security;

import com.securitytest.security.authentication.AuthenticationSuccessListener;
import com.securitytest.web.entities.SysPermission;
import com.securitytest.web.entities.SysUser;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 当认证成功后，会触发此实现类的方法 successListener
 */
@Slf4j
@Component
public class MenuAuthenticationSuccessListener implements AuthenticationSuccessListener {
    
    /**
     * 查询用户所拥有的权限菜单
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication 当用户认证通过后，会将认证对象传入
     */
    @Override
    public void successListener(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
            , Authentication authentication) {
        log.info("查询用户所拥有的权限菜单");
        Object principal = authentication.getPrincipal();
        if(principal != null && principal instanceof SysUser){
            SysUser sysUser = (SysUser) principal;
            loadMenuTree(sysUser);
        }
    }
    
    /**
     * 只加载菜单，不需要按钮
     * @param sysUser
     */
    public void loadMenuTree(SysUser sysUser) {
        //获取到了当前登录用户的菜单和按钮
        List<SysPermission> permissions = sysUser.getPermissions();
        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }
        //1.获取权限集合中所有的菜单，不要按钮
        List<SysPermission> menuList = Lists.newArrayList();
        for (SysPermission permission : permissions) {
            if (permission.getType().equals(1)) {
                menuList.add(permission);
            }
        }
        //2.获取每个菜单的子菜单
        for (SysPermission menu : menuList) {
            //存放当前菜单的所有子菜单
            List<SysPermission> childMenu = Lists.newArrayList();
            List<String> childUrl = Lists.newArrayList();
            //获取menu菜单下的所有子菜单
            for (SysPermission p : menuList) {
                //如果p.parentId等于menu.id则是他的子菜单
                if (p.getParentId().equals(menu.getId())) {
                    childMenu.add(p);
                    childUrl.add(p.getUrl());
                }
            }
            //设置子菜单
            menu.setChildren(childMenu);
            menu.setChildrenUrl(childUrl);
        }
        //3.menuList 里面每个SysPermission都有一个子菜单Children集合
        List<SysPermission> result = Lists.newArrayList();
        for (SysPermission menu : menuList) {
            //如果父Id是0，则是根菜单
            if (menu.getParentId().equals(0L)) {
                result.add(menu);
            }
        }
        sysUser.setPermissions(result);
    }

}
