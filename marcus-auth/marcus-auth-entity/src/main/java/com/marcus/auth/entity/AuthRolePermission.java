package com.marcus.auth.entity;

/**
 * @ClassName AuthRolePermission
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/10 22:31
 * @Version 1.0
 **/

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName("auth_role_permission")
@Setter
@Getter
@Accessors(chain = true)
public class AuthRolePermission implements Serializable {
    private static final long serialVersionUID = -6556148118751354229L;

    // 主键id
    private Long id;
    // 角色id
    private Long roleId;
    // 权限id
    private Long permissionId;
}
