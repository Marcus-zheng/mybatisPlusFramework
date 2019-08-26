package com.marcus.auth.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName AuthRolePermissionVo
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/14 11:46
 * @Version 1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class AuthRolePermissionVo implements Serializable {
    private static final long serialVersionUID = 4543016860774756068L;

    // 主键id
    private Long id;
    // 角色id
    private Long roleId;
    // 权限id
    private Long permissionId;
}
