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
@TableName("auth_role")
@Setter
@Getter
@Accessors(chain = true)
public class AuthRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4689988259627651710L;

    // 角色编号
    private String code;
    // 角色名称
    private String name;
    // 备注
    private String remark;

    public AuthRole(){
        super();
    }

    public AuthRole(String code, String name, String remark){
        super();
        this.code = code;
        this.name = name;
        this.remark = remark;
    }
}
