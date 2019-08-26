package com.marcus.auth.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName AuthUserRoleVo
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/14 11:46
 * @Version 1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class AuthUserRoleVo implements Serializable {
    private static final long serialVersionUID = 8562969587035297001L;

    // 主键id
    private Long id;
    // 用户id
    private Long userId;
    // 角色id
    private Long roleId;
}
