package com.securitytest.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.securitytest.web.entities.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 通过用户id查询所拥有的角色
     * @param userId
     * @return
     */
    List<SysRole> findByUserId(@Param("userId") Long userId);
}
