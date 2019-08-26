package com.marcus.auth.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName AuthRoleVo
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/14 11:43
 * @Version 1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class AuthRoleVo implements Serializable {
    private static final long serialVersionUID = 8336075990047940351L;

    // 主键id
    private Long id;
    // 角色编号
    private String code;
    // 角色名称
    private String name;
    // 备注
    private String remark;
    // 用户关联角色id;
    private String permissionIds;

    public AuthRoleVo(){
        super();
    }

    public AuthRoleVo(String code, String name, String remark){
        super();
        this.code = code;
        this.name = name;
        this.remark = remark;
    }
}
