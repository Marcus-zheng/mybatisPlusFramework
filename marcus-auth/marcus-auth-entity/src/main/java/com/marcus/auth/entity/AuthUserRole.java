package com.marcus.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName AuthUserRole
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/10 21:15
 * @Version 1.0
 **/
@TableName("auth_user_role")
@Setter
@Getter
@Accessors(chain = true)
public class AuthUserRole implements Serializable {
    private static final long serialVersionUID = 3057146478055238634L;

    // 主键id
    private Long id;
    // 用户id
    private Long userId;
    // 角色id
    private Long roleId;
}
