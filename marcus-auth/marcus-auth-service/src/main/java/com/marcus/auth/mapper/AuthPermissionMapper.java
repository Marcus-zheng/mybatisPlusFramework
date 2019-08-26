package com.marcus.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marcus.auth.entity.AuthPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthPermissionMapper extends BaseMapper<AuthPermission> {

    @Select("SELECT * FROM auth_permission e LEFT JOIN auth_role_permission arp ON e.id = arp.permissionId " +
            "LEFT JOIN auth_user_role aur ON arp.roleId = aur.roleId " +
            "WHERE aur.userId = #{userId} AND e.resourceType = #{resourceType}")
    List<AuthPermission> getSystemMenus(@Param("userId") Long userId, @Param("resourceType") String resourceType);

    @Select("SELECT * FROM auth_permission e LEFT JOIN auth_role_permission arp ON e.id = arp.permissionId " +
            "LEFT JOIN auth_user_role aur ON arp.roleId = aur.roleId " +
            "WHERE aur.userId = #{userId} AND e.resourceType = #{resourceType} AND e.parentId = #{parentId}")
    List<AuthPermission> getChildMenus(@Param("userId") Long userId, @Param("resourceType") String resourceType, @Param("parentId") Long parentId);

    @Select("SELECT e.code FROM auth_permission e LEFT JOIN auth_role_permission arp ON e.id = arp.permissionId WHERE arp.roleId = #{roleId}")
    List<String> getRoleMenuCodes(@Param("roleId") Long roleId);
}
