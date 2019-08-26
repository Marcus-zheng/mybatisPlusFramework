package com.marcus.auth.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName AuthUserVo
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/14 11:46
 * @Version 1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class AuthUserVo implements Serializable {
    private static final long serialVersionUID = -4144123752039137780L;

    // 主键id
    private Long id;
    // 登录账号
    private String loginName;
    // 登录密码
    private String loginPwd;
    // 用户姓名
    private String name;
    // 用户手机号码
    private String phone;
    // 用户邮箱
    private String email;
    // 是否是超级用户 0：否，1：是
    private Integer isSuperuser;
    // 初始化标识 0：否，1：是
    private Integer initFlag;
    // 用户关联角色id
    private String roleIds;
}
