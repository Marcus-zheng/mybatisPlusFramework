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
@TableName(value = "auth_permission")
@Setter
@Getter
@Accessors(chain = true)
public class AuthPermission extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3724497891446113643L;

    // 权限编码
    private String code;
    // 权限名称
    private String name;
    // 功能权限安全编码
    private String permission;
    // 资源类型，分系统、菜单、按钮【'system','menu','button'】
    private String resourceType;
    // 资源路径
    private String resourceLink;
    // 排序编号，对菜单显示顺序进行控制
    private Integer orderNo;
    // 显示图标
    private String img;
    // 鼠标移上去变化图标
    private String imgHover;
    // 父权限id
    private Long parentId;

}
