package com.securitytest.web.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysRolePermission implements Serializable {
    @TableId(type = IdType.AUTO)
	private Long id;
    
    private Long roleId;
    
    private Long permissionId;
}
