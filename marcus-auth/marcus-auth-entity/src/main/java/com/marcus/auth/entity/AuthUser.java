package com.marcus.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.marcus.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description:
 * @author: Marcus.zheng
 * @date:  2019/7/10 20:32
 **/
@TableName(value = "auth_user")
@Setter
@Getter
@Accessors(chain = true)
public class AuthUser extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3000009970377767710L;

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
    // 删除标识：0：正常，1：删除
    private Integer delStatus;
}
