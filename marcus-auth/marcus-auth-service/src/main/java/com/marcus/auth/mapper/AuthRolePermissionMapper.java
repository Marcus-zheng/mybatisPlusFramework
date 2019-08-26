package com.marcus.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marcus.auth.entity.AuthRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthRolePermissionMapper extends BaseMapper<AuthRolePermission> {

    @Select("SELECT permissionId FROM auth_role_permission arp " +
            "LEFT JOIN auth_permission ap ON arp.permissionId = ap.id" +
            " where roleId = #{roleId} and ap.resourceType != 'system'")
    List<Long> getPermissionIdsByRoleId(@Param("roleId") Long roleId);
}
